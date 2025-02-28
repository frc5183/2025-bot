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
     * Updates all cameras with the most recent results
     */
    fun updateUnreadResults()

    /**
     * Updates the robot pose in the IO (mainly used for simulation, however
     * implementers can use this for other purposes).
     * @param pose The new robot pose.
     */
    fun updateRobotPose(pose: Pose2d)

    /**
     * Gets the [EstimatedRobotPose] from [camera].
     * @param camera The [camera] to get the robot pose from.
     * @return The estimated robot [Pose2d] from the [camera], or null if the pose
     * could not be estimated or it is not "accurate."
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
     * Returns the distance to a [target] in relation to the center of the robot.
     * @param target The [PhotonTrackedTarget] to get the distance to.
     * @return The distance to [target], or null if [target] is not visible.
     */
    fun getDistanceFromTarget(target: PhotonTrackedTarget): Distance?

    /**
     * Returns the closest visible [PhotonTrackedTarget] that has the proper id for [target].
     * @param target The [FieldTarget] to look for.
     * @return The closest possible [PhotonTrackedTarget] with an id that [target] has, or null
     * if no visible targets has an id that [target] has (alliance-specific).
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
