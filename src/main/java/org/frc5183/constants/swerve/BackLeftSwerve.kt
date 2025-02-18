package org.frc5183.constants.swerve

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units

object BackLeftSwerve {
    val location = Translation2d(Units.Inches.of(13.5), Units.Inches.of(13.5))
    val driveMotorId = 1
    val angleMotorId = 2
    val driveMotorReversed = false
    val angleMotorReversed = false
    val absoluteEncoderId = 3
    val absoluteEncoderOffset = 9102830
    val absoluteEncoderReversed = false
}