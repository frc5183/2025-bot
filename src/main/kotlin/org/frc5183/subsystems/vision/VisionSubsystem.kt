package org.frc5183.subsystems.vision

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.hardware.vision.Camera
import org.frc5183.subsystems.vision.io.VisionIO
import org.frc5183.target.FieldTarget
import org.littletonrobotics.junction.Logger
import org.photonvision.EstimatedRobotPose
import org.photonvision.targeting.PhotonTrackedTarget

class VisionSubsystem(
    val io: VisionIO,
) : Subsystem {
    private val ioInputs = VisionIO.VisionIOInputs()

    /**
     * All cameras used by the vision subsystem.
     * @see [VisionIO.cameras]
     */
    val cameras: List<Camera>
        get() = io.cameras

    /**
     * All targets in view of any camera.
     * @see [VisionIO.visibleTargets]
     */
    val visibleTargets: List<FieldTarget>
        get() = io.visibleTargets

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Vision", ioInputs)
        io.updateUnreadResults()
    }

    /**
     * Updates the robot's pose.
     * @param pose The new robot [Pose2d].
     * @see [VisionIO.updateRobotPose]
     */
    fun updateRobotPose(pose: Pose2d) = io.updateRobotPose(pose)

    /**
     * Gets the [EstimatedRobotPose] from [camera].
     * @param camera The [camera] to get the robot pose from.
     * @return The estimated robot [Pose2d] from the [camera], or null if the pose
     * could not be estimated or it is not "accurate."
     * @see [VisionIO.getEstimatedRobotPose]
     */
    fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? = io.getEstimatedRobotPose(camera)

    /**
     * Returns the nearest [FieldTarget] that any camera can see.
     * @return The nearest target that any camera can see, or null if no targets are in view.
     * @see [VisionIO.getNearestTarget]
     */
    fun getNearestTarget(): FieldTarget? = io.getNearestTarget()

    /**
     * Gets the distance from the robot to the target.
     * @param target The target to get the distance to.
     * @return The distance from the robot to the target, or null if the target is not in view.
     * @see [VisionIO.getDistanceFromTarget]
     */
    fun getDistanceFromTarget(target: FieldTarget): Distance? = io.getDistanceFromTarget(target)

    /**
     * Returns the distance to a [target] in relation to the center of the robot.
     * @param target The [PhotonTrackedTarget] to get the distance to.
     * @return The distance to [target], or null if [target] is not visible.
     * @see [VisionIO.getDistanceFromTarget]
     */
    fun getDistanceFromTarget(target: PhotonTrackedTarget): Distance? = io.getDistanceFromTarget(target)

    /**
     * Returns the closest visible [PhotonTrackedTarget] that has the proper id for [target].
     * @param target The [FieldTarget] to look for.
     * @return The closest possible [PhotonTrackedTarget] with an id that [target] has, or null
     * if no visible targets has an id that [target] has (alliance-specific).
     * @see [VisionIO.getClosestVisiblePhotonTarget]
     */
    fun getClosestVisiblePhotonTarget(target: FieldTarget): PhotonTrackedTarget? = io.getClosestVisiblePhotonTarget(target)

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [target].
     * @param target The [PhotonTrackedTarget] to aim at.
     * @return The [Pose2d] the robot should be at to aim at the [target], or
     * null if the [target] is not in view.
     * @see [VisionIO.getAimPose]
     */
    fun getAimPose(target: PhotonTrackedTarget): Pose2d? = io.getAimPose(target)

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [target].
     * @param target The [FieldTarget] to aim at.
     * @return The [Pose2d] the robot should be at to aim at the [target], or
     * null if the [target] is not in view.
     * @see [VisionIO.getAimPose]
     */
    fun getAimPose(target: FieldTarget): Pose2d? = io.getAimPose(target)
}
