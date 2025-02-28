package org.frc5183.constants

import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.units.*
import edu.wpi.first.units.measure.*

object PhysicalConstants {
    /**
     * The [Mass] of the robot.
     */
    val MASS: Mass = Units.Pounds.of(110.2311)

    /**
     * The [MomentOfInertia] of the robot.
     */
    val MOI: MomentOfInertia = Units.KilogramSquareMeters.of(6.883)

    // <editor-fold desc="Maximum Constraints">

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

    /**
     * A measure of [Voltage] over [Time] the swerve module's drive motor can ramp at.
     */
    // todo: find a way to use PerUnit to represent ramp rates better.
    const val DRIVE_MOTOR_RAMP_RATE = 0.25
    // val DRIVE_MOTOR_RAMP_RATE: Measure<out PerUnit<VoltageUnit, TimeUnit>> = PerUnit.combine(Units.Volts, Units.Seconds).of(0.25)

    /**
     * A measure of [Voltage] over [Time] the swerve module's angle motor can ramp at.
     */
    const val ANGLE_MOTOR_RAMP_RATE = 0.25
    // val ANGLE_MOTOR_RAMP_RATE: Measure<out PerUnit<VoltageUnit, TimeUnit>> = PerUnit.combine(Units.Volts, Units.Seconds).of(0.25)

    // </editor-fold>

    /**
     * The type of [DCMotor] used for the swerve module's drive motor.
     */
    val DRIVE_MOTOR_TYPE: DCMotor = DCMotor.getFalcon500(1)

    /**
     * The type of [DCMotor] used for the swerve module's angle motor.
     */
    val ANGLE_MOTOR_TYPE: DCMotor = DCMotor.getFalcon500(1)

    /**
     * The minimum [Voltage] the swerve module's drive motor can run at.
     */
    val DRIVE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(300.0)

    /**
     * The minimum [Voltage] the swerve module's angle motor can run at.
     */
    val ANGLE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(200.0)

    /**
     * The rotational inertia of the swerve module's drive motor.
     */
    val STEER_ROTATIONAL_INERTIA: MomentOfInertia = Units.KilogramSquareMeters.of(0.03)

    /**
     * The optimal [Voltage] the robot should run at.
     */
    val OPTIMAL_VOLTAGE: Voltage = Units.Volts.of(12.0)

    /**
     * The gear ratio of the swerve module's angle motor.
     */
    const val ANGLE_GEAR_RATIO: Double = 18.75

    /**
     * The gear ratio of the swerve module's drive motor.
     */
    const val DRIVE_GEAR_RATIO: Double = 5.9

    /**
     * The [Current] limit of the swerve module's angle motor.
     */
    val ANGLE_CURRENT_LIMIT: Current = Units.Amps.of(30.0)

    /**
     * The [Current] limit of the swerve module's drive motor.
     */
    val DRIVE_CURRENT_LIMIT: Current = Units.Amps.of(40.0)

    /**
     * The coefficient of friction of the swerve module's wheels.
     */
    const val WHEEL_COF: Double = 1.0

    /**
     * The [Distance] of the swerve module's wheel radius.
     */
    val WHEEL_RADIUS: Distance = Units.Inches.of(4.0)
}
