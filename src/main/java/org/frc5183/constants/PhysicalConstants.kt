package org.frc5183.constants

import com.pathplanner.lib.config.PIDConstants
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.*

object PhysicalConstants {
    val FRONT_LEFT_MODULE_OFFSET: Translation2d = Translation2d(1.0, 1.0)
    val FRONT_RIGHT_MODULE_OFFSET: Translation2d = Translation2d(1.0, -1.0)
    val BACK_LEFT_MODULE_OFFSET: Translation2d = Translation2d(-1.0, 1.0)
    val BACK_RIGHT_MODULE_OFFSET: Translation2d = Translation2d(-1.0, -1.0)
    val DRIVE_CURRENT_LIMIT = Units.Amps.of(40.0)
    val DRIVE_MOTOR = DCMotor.getFalcon500(1)
    const val WHEEL_COF: Double = 1.0
    val WHEEL_RADIUS: Distance = Units.Meters.of(0.1)

    /**
     * The [Mass] of the robot.
     */
    val MASS: Mass = Units.Kilograms.of(50.0)

    /**
     * The [MomentOfInertia] of the robot.
     */
    val MOI: MomentOfInertia = Units.KilogramSquareMeters.of(1.0)

    /**
     * The maximum [LinearVelocity] of the robot.
     */
    val MAX_SPEED: LinearVelocity = Units.MetersPerSecond.of(3.0)

    /**
     * The maximum [LinearAcceleration] of the robot.
     */
    val MAX_ACCELERATION: LinearAcceleration = Units.MetersPerSecondPerSecond.of(3.0)

    /**
     * The maximum [AngularVelocity] of the robot.
     */
    val MAX_ANGULAR_VELOCITY: AngularVelocity = Units.RadiansPerSecond.of(180.0)

    /**
     * The maximum [AngularAcceleration] of the robot.
     */
    val MAX_ANGULAR_ACCELERATION: AngularAcceleration = Units.RadiansPerSecondPerSecond.of(360.0)

    val ROTATION_PID_CONSTANTS: PIDConstants = PIDConstants(5.0)
    val TRANSLATION_PID_CONSTANTS: PIDConstants = PIDConstants(5.0)
}
