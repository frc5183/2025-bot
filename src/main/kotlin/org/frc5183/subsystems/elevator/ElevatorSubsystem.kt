package org.frc5183.subsystems.elevator

import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.Timer
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.io.ElevatorIO
import org.littletonrobotics.junction.Logger

class ElevatorSubsystem(
    val io: ElevatorIO,
) : Subsystem {
    private val ioInputs: ElevatorIO.ElevatorIOInputs = ElevatorIO.ElevatorIOInputs()

    //private val encoderZero: Angle = io.motorEncoder

    /**
     * The desired stage the elevator should be on.
     */
    var desiredStage: Int = 0

    /**
     * The current stage the elevator is on based on the motor's encoder.
     */
    var currentStage: Int = 0
        private set

    /**
     * The absolute difference between the current encoder value and the current stage's
     * desired encoder value.
     */
    val stageDrift: Angle
        get() = (Config.ELEVATOR_STAGES.getOrNull(desiredStage) ?: Units.Degrees.of(0.0)) - io.motorEncoder

    val bottomLimitSwitch: Boolean
        get() = io.bottomLimitSwitchTriggered
    
    val motorRunningUp: Boolean
        get() = speedMovesUp(io.motorSpeed)

    val motorRunningDown: Boolean
        get() = speedMovesDown(io.motorSpeed)

    override fun periodic() {
        io.updateInputs(ioInputs, currentStage)
        Logger.processInputs("Elevator", ioInputs)

        currentStage = Config.ELEVATOR_STAGES.indexOfFirst { it >= io.motorEncoder }

        if (bottomLimitSwitch) {
          currentStage = 0
          if (motorRunningDown) stopElevator()
        }
    }

    /**
     * Runs the elevator at [speed]
     */
    fun runElevator(speed: Double) {
      if (!bottomLimitSwitch) io.runElevator(speed)

      if (speedMovesUp(speed) && bottomLimitSwitch) io.runElevator(speed)
    }
  

    /**
     * Runs the elevator motor up.
     */
    fun raiseElevator(speed: Double) = runElevator(-1.0 * speed)

    /**
     * Runs the elevator motor down.
     */
    fun lowerElevator(speed: Double) = runElevator(1.0 * speed)

    /**
     * Stops the elevator motor.
     */
    fun stopElevator() = io.stopElevator()

    /**
     * Resets the motor encoder to 0.
     * Should be called when the bottom limit switch is triggered.
     */
    fun resetEncoder() = io.resetEncoder()

    /**
     * Whether the given speed will move the elevator down.
     *
     * @param speed The speed to check.
     * @return Whether the speed will move the elevator down.
     */
    fun speedMovesDown(speed: Double): Boolean =
        speed > 0 && Config.ELEVATOR_MOTOR_INVERTED || speed < 0 && !Config.ELEVATOR_MOTOR_INVERTED

    /**
     * Whether the given speed will move the elevator up.
     *
     * @param speed The speed to check.
     * @return Whether the speed will move the elevator up.
     */
    fun speedMovesUp(speed: Double): Boolean = !speedMovesDown(speed)
}

