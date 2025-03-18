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

    override fun updateInputs(inputs: VisionIO.VisionIOInputs) {
        val enabledNames = cameras.map { it.name }
        inputs.camerasEnabled =
            MutableList(maxOf(inputs.camerasEnabled.size, enabledNames.size)) {
                enabledNames.getOrNull(it) ?: "null"
            }.toTypedArray()

        val activeNames = cameras.filter { it.isConnected }.map { it.name }
        inputs.camerasActive =
            MutableList(maxOf(inputs.camerasActive.size, activeNames.size)) {
                activeNames.getOrNull(it) ?: "null"
            }.toTypedArray()

        val withTargetsNames = cameras.filter { it.visibleTargets.isNotEmpty() }.map { it.name }
        inputs.camerasWithTargets =
            MutableList(maxOf(inputs.camerasWithTargets.size, withTargetsNames.size)) {
                withTargetsNames.getOrNull(it) ?: "null"
            }.toTypedArray()

        val targetNames = visibleTargets.map { it.name }
        inputs.visibleTargets =
            MutableList(maxOf(inputs.visibleTargets.size, targetNames.size)) {
                targetNames.getOrNull(it) ?: "null"
            }.toTypedArray()
    }

    override fun updateUnreadResults() {
        cameras.forEach { it.updateUnreadResults() }
    }

    override fun updateRobotPose(pose: Pose2d) {
        // This does nothing here.
    }

    override fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? = camera.estimatedRobotPose

    override fun getNearestTarget(): FieldTarget? =
        visibleTargets.minByOrNull {
            getDistanceFromTarget(it)?.`in`(Units.Meter) ?: Double.POSITIVE_INFINITY
        }

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

    override fun getDistanceFromTarget(target: PhotonTrackedTarget): Distance? {
        val fieldTarget = FieldTarget.getTarget(target.fiducialId) ?: return null
        return getDistanceFromTarget(fieldTarget)
    }

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

    override fun getAimPose(target: PhotonTrackedTarget): Pose2d? {
        val fieldTarget = FieldTarget.getTarget(target.fiducialId) ?: return null

        return FieldTarget.getPose(target.fiducialId)?.toPose2d()?.transformBy(fieldTarget.desired)
    }

    override fun getAimPose(target: FieldTarget): Pose2d? {
        val photonTarget = getClosestVisiblePhotonTarget(target) ?: return null
        return getAimPose(photonTarget)
    }
}
