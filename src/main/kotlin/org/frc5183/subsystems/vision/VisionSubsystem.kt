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

    /**
     * Performs periodic updates for the vision subsystem.
     *
     * This method updates the vision system's input state, logs the current inputs under the "Vision" category,
     * and processes any unread results to ensure the subsystem remains synchronized with the latest sensor data.
     */
    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Vision", ioInputs)
        io.updateUnreadResults()
    }

    /**
 * Updates the robot's pose using the provided [Pose2d].
 *
 * Delegates the update to the underlying vision I/O, ensuring that the vision system has the current robot position.
 *
 * @param pose the new pose of the robot.
 * @see VisionIO.updateRobotPose
 */
    fun updateRobotPose(pose: Pose2d) = io.updateRobotPose(pose)

    /**
 * Retrieves the estimated robot pose from the specified camera.
 *
 * This method queries the vision I/O system for a robot pose estimate. It returns an
 * [EstimatedRobotPose] if a reliable estimate can be obtained, or null if the estimation
 * fails or is deemed insufficiently accurate.
 *
 * @param camera the camera used for estimating the robot's pose
 * @return the estimated robot pose as an [EstimatedRobotPose], or null if the pose could not be determined reliably
 * @see VisionIO.getEstimatedRobotPose
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
 * Retrieves the nearest visible [PhotonTrackedTarget] whose identifier matches that of the specified [FieldTarget].
 *
 * @param target the [FieldTarget] whose id (typically alliance-specific) is used to locate a matching [PhotonTrackedTarget].
 * @return the closest matching [PhotonTrackedTarget] if one exists; otherwise, null.
 *
 * @see VisionIO.getClosestVisiblePhotonTarget
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
