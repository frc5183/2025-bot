package org.frc5183.constants

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Miscellaneous configuration constants.
 */
object Config {
    val ELEVATOR_STAGES: List<Angle> = listOf(
        Units.Degrees.of(0.0), // Stage 0
        Units.Degrees.of(10.0), // Stage 1
        Units.Degrees.of(20.0), // Stage 2
        Units.Degrees.of(30.0), // Stage 3
    )
    const val VISION_POSE_ESTIMATION = false
    val FIELD_LAYOUT: AprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded) // https://firstfrc.blob.core.windows.net/frc2025/Manual/TeamUpdates/TeamUpdate12.pdf
    val BREAK_TIME_AFTER_DISABLE: Duration = 1.seconds
}
