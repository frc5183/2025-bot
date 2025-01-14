package org.frc5183.constants.swerve

import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.Filesystem
import org.frc5183.constants.PhysicalConstants
import swervelib.SwerveDrive
import swervelib.parser.SwerveParser
import java.io.File

object SwerveConstants {
    val SWERVE_DRIVE: SwerveDrive =
        SwerveParser(
            File(Filesystem.getDeployDirectory(), "swerve"),
        ).createSwerveDrive(PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond))

    /*
   todo
    val SWERVE_DRIVE: SwerveDrive = SwerveDrive(
        SwerveDriveConfiguration(
            listOf(
                SwerveModuleConfiguration(
                    FrontLeftConstants.DRIVE_MOTOR,
                    FrontLeftConstants.ANGLE_MOTOR,
                    ConversionFactorsJson(

                    ),
                    FrontLeftConstants.ABSOLUTE_ENCODER,
                    FrontLeftConstants.ABSOLUTE_ENCODER_OFFSET,
                    FrontLeftConstants.LOCATION.x,
                    FrontLeftConstants.LOCATION.y,
                    asd,
                    asd,
                    SwerveModulePhysicalCharacteristics(

                    )
                ),
            ).toTypedArray(),
            NavXSwerve(
                SerialPort.Port.kMXP,
                NavXComType.kUSB
            ),
            PhysicalConstants.IMU_INVERTED,
            SwerveModulePhysicalCharacteristics()
        ),
        SwerveControllerConfiguration(

        ),
        PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond),
        Pose2d.kZero
    )
     */
}
