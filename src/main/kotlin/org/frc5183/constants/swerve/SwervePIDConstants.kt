package org.frc5183.constants.swerve

import swervelib.parser.PIDFConfig

object SwervePIDConstants {
    val DRIVE_PID: PIDFConfig =
        PIDFConfig(
            0.0020645, // P
            0.0, // I
            0.0, // D
            0.0, // F
            0.0, // IZ
        )

    val ANGLE_PID: PIDFConfig =
        PIDFConfig(
            0.0020645, // P
            0.0, // I
            0.0, // D
            0.0, // F
            0.0, // IZ
        )
}
