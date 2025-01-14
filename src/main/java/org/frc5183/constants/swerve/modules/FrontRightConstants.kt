package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.DeviceConstants
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor

object FrontRightConstants : SwerveModuleConstants {
    override val LOCATION: Translation2d
        get() = Translation2d(0.0, 0.0)

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
        get() = TODO()

    override val ABSOLUTE_ENCODER_OFFSET: Angle
        get() = Units.Degrees.of(0.0)

    override val ABSOLUTE_ENCODER_INVERTED: Boolean
        get() = TODO("Not yet implemented")
    override val DRIVE_MOTOR: SwerveMotor
        get() = TODO("Not yet implemented")
    override val DRIVE_MOTOR_INVERTED: Boolean
        get() = TODO("Not yet implemented")
    override val ANGLE_MOTOR: SwerveMotor
        get() = TODO("Not yet implemented")
    override val ANGLE_MOTOR_INVERTED: Boolean
        get() = TODO("Not yet implemented")
}
