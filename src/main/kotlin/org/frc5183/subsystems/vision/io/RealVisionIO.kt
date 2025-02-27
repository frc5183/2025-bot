package org.frc5183.subsystems.vision.io

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj.DriverStation
import org.frc5183.hardware.vision.Camera
import org.frc5183.target.FieldTarget
import org.photonvision.EstimatedRobotPose
import org.photonvision.PhotonUtils
import org.photonvision.targeting.PhotonTrackedTarget
import kotlin.jvm.optionals.getOrDefault

// todo see SimulatedSwerveDriveIO
open class RealVisionIO(
    override val cameras: List<Camera>,
) : VisionIO {
    override val visibleTargets: List<FieldTarget>
        get() = cameras.flatMap { it.visibleTargets }

    /**
     * Updates the vision input data with current camera statuses and visible target information.
     *
     * This method refreshes the arrays in the provided [VisionIO.VisionIOInputs] instance by:
     * - Populating `camerasEnabled` with the names of all cameras.
     * - Populating `camerasActive` with the names of connected cameras.
     * - Populating `camerasWithTargets` with the names of cameras that have at least one visible target.
     * - Populating `visibleTargets` with the names of all visible targets.
     *
     * Each array is resized to the maximum of its current size or the number of items,
     * inserting the string "null" for indexes where data is unavailable.
     *
     * @param inputs The vision input structure to update with current system data.
     */
    override fun updateInputs(inputs: VisionIO.VisionIOInputs) {
        val enabledNames = cameras.map { it.name }
        inputs.camerasEnabled = MutableList(maxOf(inputs.camerasEnabled.size, enabledNames.size)) {
            enabledNames.getOrNull(it) ?: "null"
        }.toTypedArray()

        val activeNames = cameras.filter { it.isConnected }.map { it.name }
        inputs.camerasActive = MutableList(maxOf(inputs.camerasActive.size, activeNames.size)) {
            activeNames.getOrNull(it) ?: "null"
        }.toTypedArray()

        val withTargetsNames = cameras.filter { it.visibleTargets.isNotEmpty() }.map { it.name }
        inputs.camerasWithTargets = MutableList(maxOf(inputs.camerasWithTargets.size, withTargetsNames.size)) {
            withTargetsNames.getOrNull(it) ?: "null"
        }.toTypedArray()

        val targetNames = visibleTargets.map { it.name }
        inputs.visibleTargets = MutableList(maxOf(inputs.visibleTargets.size, targetNames.size)) {
            targetNames.getOrNull(it) ?: "null"
        }.toTypedArray()
    }

    /**
     * Updates unread results for all cameras.
     *
     * Iterates over each camera in the list and triggers an update of its unread results, ensuring that new data is processed.
     */
    override fun updateUnreadResults() {
        cameras.forEach { it.updateUnreadResults() }
    }

    /**
     * No-op implementation of the updateRobotPose method.
     *
     * This method satisfies the VisionIO interface contract by accepting a robot pose,
     * but does not perform any operations.
     *
     * @param pose the current robot pose (unused).
     */
    override fun updateRobotPose(pose: Pose2d) {
        // This does nothing here.
    }

    /**
 * Retrieves the estimated robot pose from the specified camera.
 *
 * @param camera the camera from which to obtain the estimated pose.
 * @return the estimated robot pose if available, or null otherwise.
 */
override fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? = camera.estimatedRobotPose

    /**
         * Returns the nearest visible FieldTarget based on computed distance.
         *
         * The distance is determined by converting the output of getDistanceFromTarget to meters. Targets without
         * a measurable distance are assigned an infinite value, ensuring they are excluded from selection.
         * If there are no valid targets, null is returned.
         *
         * @return the closest FieldTarget, or null if none are available.
         */
        override fun getNearestTarget(): FieldTarget? =
        visibleTargets.minByOrNull {
            getDistanceFromTarget(it)?.`in`(Units.Meter) ?: Double.POSITIVE_INFINITY
        }

    /**
     * Computes the distance from a camera to the specified field target if it is visible and valid for the current alliance.
     *
     * The method searches for a camera that currently sees the target, then iterates over the reported photon targets to find one
     * whose fiducial ID matches the alliance-specific identifiers of the field target. If a valid photon target is found and its pose
     * is available, the distance is calculated using the camera's transform parameters and the target's pose.
     *
     * @param target the field target whose distance is being measured.
     * @return the computed distance in meters, or null if no matching camera or photon target is found.
     */
    override fun getDistanceFromTarget(target: FieldTarget): Distance? {
        val camera = cameras.find { it.visibleTargets.contains(target) } ?: return null
// Get the transform from the camera to the target
        for (photonTarget in camera.results.flatMap { it.getTargets() }) {
            if (
                !(
                    DriverStation.getAlliance().getOrDefault(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue &&
                        target.blueIds.contains(photonTarget.fiducialId)
                ) &&
                !(DriverStation.getAlliance().get() == DriverStation.Alliance.Red && target.redIds.contains(photonTarget.fiducialId))
            ) {
                continue
            }

            val targetPose = FieldTarget.getPose(photonTarget.fiducialId) ?: return null
            return Units.Meters.of(
                PhotonUtils.calculateDistanceToTargetMeters(
                    camera.transform.z,
                    targetPose.z,
                    camera.transform.rotation.y,
                    photonTarget.pitch,
                ),
            )
        }
        return null
    }

    /**
     * Retrieves the distance for a given photon tracked target.
     *
     * This method resolves the associated field target using the target's fiducial ID and then calculates
     * the distance from it. Returns null if no corresponding field target is found.
     *
     * @param target the photon tracked target.
     * @return the calculated distance, or null if the field target is not recognized.
     */
    override fun getDistanceFromTarget(target: PhotonTrackedTarget): Distance? {
        val fieldTarget = FieldTarget.getTarget(target.fiducialId) ?: return null
        return getDistanceFromTarget(fieldTarget)
    }

    /**
     * Returns the closest visible PhotonTrackedTarget for the given field target that matches the current alliance.
     *
     * This method first checks whether the specified target is included in the aggregated list of visible targets. It then iterates
     * through each camera that sees the target and examines its detected photon targets. A photon target is considered a match if its
     * fiducial ID corresponds to the target's expected IDs for the current alliance (blue or red). The first matching photon target is returned.
     *
     * @param target the field target for which to find a matching photon target.
     * @return the matching PhotonTrackedTarget if found; otherwise, null.
     */
    override fun getClosestVisiblePhotonTarget(target: FieldTarget): PhotonTrackedTarget? {
        if (!visibleTargets.contains(target)) return null

        for (camera in cameras) {
            if (!camera.visibleTargets.contains(target)) continue

            // Get the transform from the camera to the target
            for (photonTarget in camera.results.flatMap { it.getTargets() }) {
                if (
                    !(
                        DriverStation.getAlliance().getOrDefault(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue &&
                            target.blueIds.contains(photonTarget.fiducialId)
                    ) &&
                    !(
                        DriverStation.getAlliance().get() == DriverStation.Alliance.Red &&
                            target.redIds.contains(photonTarget.fiducialId)
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
     * Computes the aim pose for the specified photon tracked target.
     *
     * This method retrieves the corresponding field target using the target's fiducial ID. If the field target is found,
     * it converts the associated pose to a Pose2d and applies a transformation based on the field target’s desired offset.
     * If no valid field target or pose is present, the function returns null.
     *
     * @param target The photon tracked target whose fiducial ID is used to determine the corresponding field target.
     * @return The computed aim pose as a Pose2d, or null if the necessary field target or pose data is unavailable.
     */
    override fun getAimPose(target: PhotonTrackedTarget): Pose2d? {
        val fieldTarget = FieldTarget.getTarget(target.fiducialId) ?: return null

        return FieldTarget.getPose(target.fiducialId)?.toPose2d()?.transformBy(fieldTarget.desired)
    }

    /**
     * Computes the aim pose for the given field target.
     *
     * This method retrieves the closest visible photon tracked target associated with the specified
     * field target and computes its aim pose. If no such photon target is found, it returns null.
     *
     * @param target the field target for which to calculate the aim pose
     * @return the aim pose if a valid photon target is visible; otherwise, null
     */
    override fun getAimPose(target: FieldTarget): Pose2d? {
        val photonTarget = getClosestVisiblePhotonTarget(target) ?: return null
        return getAimPose(photonTarget)
    }
}
