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
        Units.Rotations.of(0.0),
        Units.Rotations.of(1.483),
        Units.Rotations.of(2.421),
        Units.Rotations.of(5.050),
        Units.Rotations.of(9.258)
    )
    val ELEVATOR_MAX_ALLOWED_DRIFT: Angle = Units.Degrees.of(1.0)
    const val ELEVATOR_MOTOR_INVERTED: Boolean = true
    const val ELEVATOR_MOVEMENT_SPEED = 0.5
    const val ELEVATOR_HOLD_SPEED = 0.2

    val CORAL_SHOOT_TIME: Duration = 1.seconds
    const val CORAL_PROXIMITY_THRESHOLD: Int = 200

    /**
     * The amount of time to continue running the intake motor for after
     * the coral has been detected.
     */
    val CORAL_INTAKE_TIME: Duration = 1.seconds
    const val CORAL_INTAKE_SPEED: Double = 0.5
    const val CORAL_SHOOT_SPEED: Double = 1.0

    const val VISION_POSE_ESTIMATION = false
    val FIELD_LAYOUT: AprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded) // https://firstfrc.blob.core.windows.net/frc2025/Manual/TeamUpdates/TeamUpdate12.pdf
    val BREAK_TIME_AFTER_DISABLE: Duration = 1.seconds
}
