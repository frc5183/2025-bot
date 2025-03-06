package org.frc5183.subsystems.elevator

import edu.wpi.first.units.Units
import org.frc5183.subsystems.elevator.io.ElevatorIO
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.Config
import org.littletonrobotics.junction.Logger

class ElevatorSubsystem(val io: ElevatorIO) : Subsystem {
  private val ioInputs: ElevatorIO.ElevatorIOInputs = ElevatorIO.ElevatorIOInputs()

  /**
   * The current stage the elevator is on.
   */
  var currentStage: Int = 0

  override fun periodic() {
    io.updateInputs(ioInputs, currentStage)
    Logger.processInputs("Elevator", ioInputs)

    currentStage = Config.ELEVATOR_STAGES.indexOfFirst { it > io.motorEncoder }

    if (io.bottomLimitSwitchTriggered) {
      stopElevator()
      resetEncoder()
    }

    if (io.topLimitSwitchTriggered) stopElevator()
  }

  /**
   * Runs the elevator at [speed]
   */
    fun runElevator(speed: Double) = io.runElevator(speed)

  /**
   * Runs the elevator motor up.
   */
  fun raiseElevator() = runElevator(1.0)

  /**
   * Runs the elevator motor down.
   */
  fun lowerElevator() = runElevator(-1.0)

    /**
     * Stops the elevator motor.
     */
    fun stopElevator() = io.stopElevator()

    /**
     * Resets the motor encoder to 0.
     * Should be called when the bottom limit switch is triggered.
     */
    fun resetEncoder() = io.resetEncoder()
}
