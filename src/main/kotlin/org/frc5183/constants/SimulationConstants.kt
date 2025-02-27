package org.frc5183.constants

import edu.wpi.first.math.geometry.Rotation2d
import org.photonvision.simulation.SimCameraProperties

object SimulationConstants {
    /**
     * Whether to use a wireframe in vision simulations.
     */
    const val VISION_WIREFRAME: Boolean = true

    /**
     * The properties of simulated cameras.
     */
    val CAMERA_PROPERTIES: SimCameraProperties = SimCameraProperties() // More in this::init.

    init {
        CAMERA_PROPERTIES.setCalibration(640, 480, Rotation2d.fromDegrees(100.0))
        CAMERA_PROPERTIES.setCalibError(0.25, 0.08)
        CAMERA_PROPERTIES.fps = 20.0
        CAMERA_PROPERTIES.avgLatencyMs = 35.0
        CAMERA_PROPERTIES.latencyStdDevMs = 5.0
    }
}
