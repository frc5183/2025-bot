package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.PhysicalConstants
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.motors.TalonFXSwerve

object FrontLeftSwerveModuleConstants : SwerveModuleConstants {
    override val NAME: String
        get() = "frontleft"

    override val LOCATION: Translation2d
        get() = Translation2d(Units.Inches.of(13.5), Units.Inches.of(10.4375))

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
        get() = CANCoderSwerve(30)

    override val ABSOLUTE_ENCODER_OFFSET: Double
//        get() = Units.Degrees.of(-116.19140625)
get()= 106.523

    override val ABSOLUTE_ENCODER_INVERTED: Boolean
        get() = false

    override val DRIVE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(17, true, PhysicalConstants.DRIVE_MOTOR_TYPE)

    override val DRIVE_MOTOR_INVERTED: Boolean
        get() = false

    override val ANGLE_MOTOR: SwerveMotor
        get() = TalonFXSwerve(18, false, PhysicalConstants.ANGLE_MOTOR_TYPE)

    override val ANGLE_MOTOR_INVERTED: Boolean
        get() = true
}
