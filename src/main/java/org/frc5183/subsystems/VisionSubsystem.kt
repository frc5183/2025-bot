package org.frc5183.subsystems

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.Robot
import org.frc5183.constants.Config
import org.frc5183.constants.DeviceConstants
import org.frc5183.constants.SimulationConstants
import org.frc5183.hardware.vision.Camera
import org.frc5183.target.FieldTarget
import org.photonvision.EstimatedRobotPose
import org.photonvision.simulation.PhotonCameraSim
import org.photonvision.simulation.VisionSystemSim
import org.photonvision.targeting.PhotonTrackedTarget
import kotlin.jvm.optionals.getOrDefault

object VisionSubsystem : Subsystem {
    /**
     * All [Camera]s used by vision, listed in order of priority
     */
    val cameras: List<Camera> =
        listOf(
            DeviceConstants.FRONT_CAMERA,
            DeviceConstants.BACK_CAMERA,
        )

    private val visionSim: VisionSystemSim? =
        if (Robot.simulation) VisionSystemSim("sim") else null

    /**
     * All targets in view of any camera.
     */
    val visibleTargets: List<FieldTarget>
        get() = cameras.flatMap { it.visibleTargets }

    init {
        if (Robot.simulation) {
            visionSim!!.addAprilTags(Config.FIELD_LAYOUT)

            for (camera in cameras) {
                val cameraSim = PhotonCameraSim(camera, SimulationConstants.CAMERA_PROPERTIES)
                cameraSim.enableDrawWireframe(SimulationConstants.VISION_WIREFRAME)

                visionSim.addCamera(cameraSim, camera.transform)
            }
        }
    }

    override fun periodic() {
        visionSim?.update(SwerveDriveSubsystem.pose)
        cameras.forEach {
            it.updateUnreadResults()
        }
    }

    /**
     * Gets the [EstimatedRobotPose] from [camera].
     * @param camera The [camera] to get the robot pose from.
     * @return The estimated robot [Pose2d] from the [camera], or null if the pose
     * could not be estimated or it is not "accurate."
     */
    fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? {
        val estimatedPose = camera.estimatedRobotPose

        if (visionSim != null && estimatedPose != null) {
            visionSim.debugField.getObject("estimated_pose").pose = estimatedPose.estimatedPose.toPose2d()
        }

        return estimatedPose
    }

    /**
     * Gets the distance from the robot to a target on the field based on the robot's current pose.
     * This does not do any vision processing, and only uses the robot's current pose to calculate the distance.
     * @param target The target to get the distance to.
     * @return The distance to the target in meters, or null if the target
     */
    fun getDistanceFromTarget(target: FieldTarget): Distance? {
        val photonTarget = getClosestVisiblePhotonTarget(target) ?: return null
        val aimPose = getAimPose(photonTarget) ?: return null

        return Units.Meters.of(
            SwerveDriveSubsystem.pose.translation.getDistance(aimPose.translation),
        )
    }

    /**
     * Returns the [Rotation2d] the robot needs to turn to face the target.
     * @param target The target to get the desired angle to.
     * @return The desired angle to the target.
     */
    fun getDesiredAngleToTarget(target: FieldTarget): Rotation2d? {
        TODO()
    }

    /**
     * Returns the nearest [FieldTarget] that [camera] can see.
     * @param camera The camera to get the nearest target from.
     * @return The nearest target that the camera can see, or null if no targets are in
     */
    fun getNearestTarget(camera: Camera): FieldTarget? =
        camera.visibleTargets.minByOrNull { getDistanceFromTarget(it)?.`in`(Units.Meters) ?: 0.0 }

    /**
     * Returns the closest visible [PhotonTrackedTarget] that can be related to [target].
     * @param target The [target] to find.
     * @return The closest visible [PhotonTrackedTarget] that can be related to [target],
     * or null if no visible targets can be related to [target].
     */
    fun getClosestVisiblePhotonTarget(target: FieldTarget): PhotonTrackedTarget? {
        if (!visibleTargets.contains(target)) return null

        for (camera in cameras) {
            if (!camera.visibleTargets.contains(target)) continue

            // Get the transform from the camera to the target
            for (photonTarget in camera.results.flatMap { it.getTargets() }) {
                if (
                    !(
                        DriverStation
                            .getAlliance()
                            .getOrDefault(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue &&
                            target.blueIds.contains(
                                photonTarget.fiducialId,
                            )
                    ) &&
                    !(
                        DriverStation.getAlliance().get() == DriverStation.Alliance.Red &&
                            target.redIds.contains(
                                photonTarget.fiducialId,
                            )
                    )
                ) {
                    continue
                }

                return photonTarget
            }
        }

        return null
    }

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [PhotonTrackedTarget].
     * This does not do any vision processing, and only uses the robot's current pose to calculate the distance.
     * @param target The [PhotonTrackedTarget] to aim at.
     * @return The pose the robot should be at to aim at the [target],
     * or null if the [target] does not correspond to a [FieldTarget].
     */
    fun getAimPose(target: PhotonTrackedTarget): Pose2d? {
        val fieldTarget = FieldTarget.getTarget(target.fiducialId) ?: return null

        return FieldTarget.getPose(target.fiducialId)?.toPose2d()?.transformBy(fieldTarget.desired)
    }

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [target].
     * @param target The [target] to aim at.
     * @return The pose the robot should be at to aim at the [target],
     * or null if the [target] is not in view.
     */
    fun getAimPose(target: FieldTarget): Pose2d? {
        val photonTarget = getClosestVisiblePhotonTarget(target) ?: return null
        return getAimPose(photonTarget)
    }
}
