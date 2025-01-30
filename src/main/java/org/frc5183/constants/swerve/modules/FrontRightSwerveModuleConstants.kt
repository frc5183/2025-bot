package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.PhysicalConstants
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.motors.TalonFXSwerve

object FrontRightSwerveModuleConstants : SwerveModuleConstants {
    override val NAME: String
        get() = "frontright"

    override val LOCATION: Translation2d
        get() = Translation2d(Units.Inches.of(13.5), Units.Inches.of(-10.4375))

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
        get() = CANCoderSwerve(31)

    override val ABSOLUTE_ENCODER_OFFSET: Angle
        get() = Units.Degrees.of(80.64)

    override val ABSOLUTE_ENCODER_INVERTED: Boolean
        get() = false

    override val DRIVE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(23, true, PhysicalConstants.DRIVE_MOTOR_TYPE)

    override val DRIVE_MOTOR_INVERTED: Boolean
        get() = false

    override val ANGLE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(24, false, PhysicalConstants.ANGLE_MOTOR_TYPE)

    override val ANGLE_MOTOR_INVERTED: Boolean
        get() = true
}
