package org.frc5183.constants.swerve

import edu.wpi.first.units.Units
import org.frc5183.constants.PhysicalConstants
import swervelib.parser.PIDFConfig
import swervelib.parser.SwerveControllerConfiguration

object SwerveControllerConstants {
    const val JOYSTICK_RADIUS_DEADBAND: Double = 0.5 // todo put in Controls?

    val HEADING_PID: PIDFConfig =
        PIDFConfig(
            0.4, // P
            0.0, // I
            0.01, // D
        )

    val YAGSL: SwerveControllerConfiguration =
        SwerveControllerConfiguration(
            SwerveConstants.YAGSL_CONFIG,
            HEADING_PID,
            JOYSTICK_RADIUS_DEADBAND,
            PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond),
        )
}
