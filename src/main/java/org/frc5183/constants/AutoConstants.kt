package org.frc5183.constants

import com.pathplanner.lib.config.ModuleConfig
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.units.Units

object AutoConstants {
    /**
     * The [RobotConfig] to be used in an [AutoBuilder].
     */
    val ROBOT_CONFIG =
        RobotConfig(
            PhysicalConstants.MASS,
            PhysicalConstants.MOI,
            ModuleConfig(
                PhysicalConstants.WHEEL_RADIUS,
                PhysicalConstants.MAX_SPEED,
                PhysicalConstants.WHEEL_COF,
                PhysicalConstants.DRIVE_MOTOR,
                PhysicalConstants.DRIVE_CURRENT_LIMIT,
                1, // 1 drive motor per module in swerve. TODO: Should this be a constant?
            ),
            PhysicalConstants.FRONT_LEFT_MODULE_OFFSET,
            PhysicalConstants.FRONT_RIGHT_MODULE_OFFSET,
            PhysicalConstants.BACK_LEFT_MODULE_OFFSET,
            PhysicalConstants.BACK_RIGHT_MODULE_OFFSET,
        )

    /**
     * Speed constraints to follow when driving autonomously.
     */
    val PATH_CONSTRAINTS =
        PathConstraints(
            PhysicalConstants.MAX_SPEED,
            PhysicalConstants.MAX_ACCELERATION,
            PhysicalConstants.MAX_ANGULAR_VELOCITY,
            PhysicalConstants.MAX_ANGULAR_ACCELERATION,
            Units.Volt.of(12.0),
        )
}
