package org.frc5183.subsystems

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.DeviceConstants
import org.frc5183.hardware.vision.Camera
import org.frc5183.target.FieldTarget
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

    val fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo)

    /**
     * All targets in view of any camera.
     */
    val visibleTargets: List<FieldTarget>
        get() = cameras.flatMap { getVisibleTargets(it) }

    override fun periodic() {
        // TODO: Update all camera data with up-to-date information.
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
        getVisibleTargets(camera).minByOrNull { getDistanceFromTarget(it)?.`in`(edu.wpi.first.units.Units.Meters) ?: 0.0 }

    /**
     * Returns all targets in view of the [camera].
     * @param camera The camera to get targets from.
     * @return A list of all targets in view of the camera.
     */
    fun getVisibleTargets(camera: Camera): List<FieldTarget> {
        TODO()
    }

    /**
     * Returns the closest visible [PhotonTrackedTarget] that can be related to [target].
     * @param target The [target] to find.
     * @return The closest visible [PhotonTrackedTarget] that can be related to [target],
     * or null if no visible targets can be related to [target].
     */
    fun getClosestVisiblePhotonTarget(target: FieldTarget): PhotonTrackedTarget? {
        if (!visibleTargets.contains(target)) return null

        for (camera in cameras) {
            if (!getVisibleTargets(camera).contains(target)) continue

            // Get the transform from the camera to the target
            for (photonTarget in camera.targets) {
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

        return FieldTarget.getPose(target.fiducialId)?.transformBy(fieldTarget.desired)
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
