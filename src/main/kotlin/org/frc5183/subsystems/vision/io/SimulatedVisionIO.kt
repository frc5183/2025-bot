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

    /**
     * Updates the simulated vision system with the robot's current pose.
     *
     * @param pose The updated robot position and orientation.
     */
    override fun updateRobotPose(pose: Pose2d) {
        visionSim.update(pose)
    }

    /**
     * Retrieves the estimated robot pose from the specified camera.
     *
     * If the camera provides a valid estimated pose, this method converts the pose into a Pose2d
     * and updates the simulation's debug field accordingly before returning it.
     *
     * @param camera The camera for which the estimated robot pose is obtained.
     * @return The current estimated robot pose, or null if no estimate is available.
     */
    override fun getEstimatedRobotPose(camera: Camera): EstimatedRobotPose? {
        val estimatedPose = camera.estimatedRobotPose

        if (estimatedPose != null) {
            visionSim.debugField.getObject("estimated_pose").pose = estimatedPose.estimatedPose.toPose2d()
        }

        return estimatedPose
    }
}
