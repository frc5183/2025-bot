package org.frc5183.constants

import edu.wpi.first.math.geometry.Transform3d
import org.frc5183.hardware.vision.Camera
import org.photonvision.PhotonCamera

/**
 * Device IDs connected to the robot.
 */
object DeviceConstants {
    val FRONT_CAMERA = Camera(PhotonCamera("Front"), Transform3d.kZero)
    val BACK_CAMERA = Camera(PhotonCamera("Back"), Transform3d.kZero)
    const val DRIVE_VISION = true
}
