package org.frc5183.subsystems.vision.io

import edu.wpi.first.math.geometry.Pose2d
import org.frc5183.constants.Config
import org.frc5183.constants.SimulationConstants
import org.frc5183.hardware.vision.Camera
import org.photonvision.EstimatedRobotPose
import org.photonvision.simulation.PhotonCameraSim
import org.photonvision.simulation.VisionSystemSim

// todo see SimulatedSwerveDriveIO
class SimulatedVisionIO(
    cameras: List<Camera>,
) : RealVisionIO(cameras) {
    val visionSim: VisionSystemSim = VisionSystemSim("sim")

    init {
        visionSim.addAprilTags(Config.FIELD_LAYOUT)

        for (camera in cameras) {
            val cameraSim = PhotonCameraSim(camera, SimulationConstants.CAMERA_PROPERTIES)
            cameraSim.enableDrawWireframe(SimulationConstants.VISION_WIREFRAME)

            visionSim.addCamera(cameraSim, camera.transform)
        }
    }

    override fun updateRobotPose(pose: Pose2d) {
        visionSim.update(pose)
    }

    override fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? {
        val estimatedPose = camera.estimatedRobotPose

        if (estimatedPose != null) {
            visionSim.debugField.getObject("estimated_pose").pose = estimatedPose.estimatedPose.toPose2d()
        }

        return estimatedPose
    }
}
