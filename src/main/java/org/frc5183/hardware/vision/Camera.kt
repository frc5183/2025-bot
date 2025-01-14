package org.frc5183.hardware.vision

import edu.wpi.first.math.geometry.Transform3d
import org.photonvision.PhotonCamera
import org.photonvision.targeting.PhotonTrackedTarget

class Camera(
    /**
     * The [PhotonCamera] this [Camera] is representing
     */
    val photonCamera: PhotonCamera,
    /**
     * The [Transform3d] of the [Camera] in relation to the robot's origin.
     */
    val transform: Transform3d,
) {
    val targets: List<PhotonTrackedTarget>
        get() = TODO()
}
