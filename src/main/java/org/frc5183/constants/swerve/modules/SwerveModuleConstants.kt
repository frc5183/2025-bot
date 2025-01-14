package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.measure.Angle
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor

interface SwerveModuleConstants {
    /**
     * The [Translation2d] of the module relative to the center of the robot.
     */
    val LOCATION: Translation2d

    /**
     * The [SwerveAbsoluteEncoder] used to measure the angle of the module.
     */
    val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder

    /**
     * The offset of the absolute encoder from the zero position.
     */
    val ABSOLUTE_ENCODER_OFFSET: Angle

    /**
     * Whether the absolute encoder is inverted.
     */
    val ABSOLUTE_ENCODER_INVERTED: Boolean

    /**
     * The [SwerveMotor] used to drive the module.
     */
    val DRIVE_MOTOR: SwerveMotor

    /**
     * Whether the drive motor is inverted.
     */
    val DRIVE_MOTOR_INVERTED: Boolean

    /**
     * The [SwerveMotor] used to rotate the module.
     */
    val ANGLE_MOTOR: SwerveMotor

    /**
     * Whether the angle motor is inverted.
     */
    val ANGLE_MOTOR_INVERTED: Boolean
}
