package org.frc5183.subsystems.vision.io

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.units.measure.Distance
import org.frc5183.hardware.vision.Camera
import org.frc5183.log.AutoLogInputs
import org.frc5183.target.FieldTarget
import org.photonvision.EstimatedRobotPose
import org.photonvision.targeting.PhotonTrackedTarget

interface VisionIO {
    class VisionIOInputs : AutoLogInputs() {
        /**
         * All the cameras used by vision.
         */
        var camerasEnabled by log(arrayOf<String>())

        /**
         * All the cameras which are properly connected and usable.
         */
        var camerasActive by log(arrayOf<String>())

        /**
         * All the cameras that currently have visible targets.
         */
        var camerasWithTargets by log(arrayOf<String>())

        /**
         * The visible targets.
         */
        var visibleTargets by log(arrayOf<String>())
    }

    /**
     * Updates the inputs for the vision subsystem logs.
     * @param inputs The [VisionIOInputs] to update.
     */
    fun updateInputs(inputs: VisionIOInputs)

    /**
     * All cameras used by the vision subsystem.
     */
    val cameras: List<Camera>

    /**
     * All targets in view of any camera.
     */
    val visibleTargets: List<FieldTarget>

    /**
 * Refreshes all cameras with the latest unread vision processing results.
 *
 * This method ensures that each camera's state is updated with any new data from the vision processing pipeline.
 */
    fun updateUnreadResults()

    /**
 * Updates the robot's pose in the IO subsystem.
 *
 * Although primarily intended for simulation, this method allows implementers to update the robot's pose
 * for logging, state management, or other custom purposes.
 *
 * @param pose The new pose to apply to the robot.
 */
    fun updateRobotPose(pose: Pose2d)

    /**
 * Returns the estimated robot pose derived from the specified camera.
 *
 * @param camera the camera used to estimate the robot pose.
 * @return an [EstimatedRobotPose] representing the robot's pose, or null if the pose
 * could not be estimated or is not considered sufficiently accurate.
 */
    fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose?

    /**
     * Returns the nearest [FieldTarget] that any camera can see.
     * @return The nearest target that any camera can see, or null if no targets are in view.
     */
    fun getNearestTarget(): FieldTarget?

    /**
     * Returns the distance to a [target] in relation to the center of the robot.
     * @param target The [FieldTarget] to get the distance to.
     * @return The distance to [target], or null if [target] is not visible.
     */
    fun getDistanceFromTarget(target: FieldTarget): Distance?

    /**
 * Calculates the distance from the robot's center to the specified [PhotonTrackedTarget].
 *
 * @param target the [PhotonTrackedTarget] for which the distance is computed.
 * @return a [Distance] representing the distance to the target, or null if the target is not visible.
 */
    fun getDistanceFromTarget(target: PhotonTrackedTarget): Distance?

    /**
 * Returns the closest visible [PhotonTrackedTarget] whose identifier matches that of the provided [FieldTarget].
 *
 * @param target the [FieldTarget] whose identifier is used for matching.
 * @return the closest matching [PhotonTrackedTarget], or null if no visible target matches the identifier (alliance-specific).
 */
    fun getClosestVisiblePhotonTarget(target: FieldTarget): PhotonTrackedTarget?

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [target].
     * @param target The [PhotonTrackedTarget] to aim at.
     * @return The [Pose2d] the robot should be at to aim at the [target], or
     * null if the [target] is not in view.
     */
    fun getAimPose(target: PhotonTrackedTarget): Pose2d?

    /**
     * Returns the [Pose2d] the robot should be at to aim at the [target].
     * @param target The [FieldTarget] to aim at.
     * @return The [Pose2d] the robot should be at to aim at the [target], or
     * null if the [target] is not in view.
     */
    fun getAimPose(target: FieldTarget): Pose2d?
}
