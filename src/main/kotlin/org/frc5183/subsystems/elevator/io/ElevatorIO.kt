package org.frc5183.subsystems.elevator.io

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.log.AutoLogInputs

interface ElevatorIO {
  class ElevatorIOInputs: AutoLogInputs() {
    /**
     * The encoder value of the main motor driving the elevator in rotations.
     */
    var motorEncoder by log(0.0)

    /**
     * The current stage of the elevator, starting at 0.
     */
    var currentStage by log(0)

    /**
     * Whether the bottom limit switch is triggered.
     */
    var bottomLimitSwitchTriggered by log(false)
  }

  val motorEncoder: Angle
  val bottomLimitSwitchTriggered: Boolean

  /**
   * Updates all [inputs] with the correct values for logging.
   *
   * @param inputs The inputs to update.
   * @param currentStage The current stage of the elevator, as it's updated
   * periodically by subsystem logic.
   */
  fun updateInputs(inputs: ElevatorIOInputs, currentStage: Int)

  /**
   * Runs the elevator at [speed].
   */
  fun runElevator(speed: Double)

  /**
   * Stops the elevator motor.
   */
    fun stopElevator()

  /**
  * Resets the motor encoder to 0.
   * Should be called when the bottom limit switch is triggered.
   */
    fun resetEncoder()

}
