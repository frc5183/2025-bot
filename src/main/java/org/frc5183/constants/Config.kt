package org.frc5183.constants

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Miscellaneous configuration constants.
 */
object Config {
    const val VISION_POSE_ESTIMATION = false // todo: enable once we have vision hardware
    val FIELD_LAYOUT: AprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025Reefscape)
    val BREAK_TIME_AFTER_DISABLE: Duration = 1.seconds
}
