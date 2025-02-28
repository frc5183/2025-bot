package org.frc5183.constants.swerve

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.units.Units
import org.frc5183.constants.DeviceConstants
import org.frc5183.constants.PhysicalConstants
import org.frc5183.constants.swerve.modules.BackLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.BackRightSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontRightSwerveModuleConstants
import swervelib.SwerveDrive
import swervelib.imu.NavXSwerve
import swervelib.imu.SwerveIMU
import swervelib.parser.SwerveDriveConfiguration

object SwerveConstants {
    val IMU: SwerveIMU = NavXSwerve(DeviceConstants.IMU_PORT)
    const val IMU_INVERTED: Boolean = false

    val YAGSL_CONFIG: SwerveDriveConfiguration =
        SwerveDriveConfiguration(
            listOf(
                FrontLeftSwerveModuleConstants.YAGSL,
                FrontRightSwerveModuleConstants.YAGSL,
                BackLeftSwerveModuleConstants.YAGSL,
                BackRightSwerveModuleConstants.YAGSL,
            ).toTypedArray(),
            IMU,
            IMU_INVERTED,
            SwerveModulePhysicalConstants.YAGSL,
        )

    val YAGSL_DRIVE: SwerveDrive =
        SwerveDrive(
            YAGSL_CONFIG,
            SwerveControllerConstants.YAGSL,
            PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond),
            Pose2d.kZero, // See SwerveDriveSubsystem to set a pose.
        )
}
