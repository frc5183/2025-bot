package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.PhysicalConstants
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.motors.TalonFXSwerve

object BackRightSwerveModuleConstants : SwerveModuleConstants {
    override val NAME: String
        get() = "backright"

    override val LOCATION: Translation2d
        get() = Translation2d(Units.Inches.of(-12.5), Units.Inches.of(-10.4375))

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
        get() = CANCoderSwerve(33)

    override val ABSOLUTE_ENCODER_OFFSET: Angle
        get() = Units.Degrees.of(0.225098)

    override val ABSOLUTE_ENCODER_INVERTED: Boolean
        get() = false

    override val DRIVE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(22, true, PhysicalConstants.DRIVE_MOTOR_TYPE)

    override val DRIVE_MOTOR_INVERTED: Boolean
        get() = false

    override val ANGLE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(21, false, PhysicalConstants.ANGLE_MOTOR_TYPE)

    override val ANGLE_MOTOR_INVERTED: Boolean
        get() = true
}
