package org.frc5183.subsystems.elevator

import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.io.ElevatorIO
import org.littletonrobotics.junction.Logger

class ElevatorSubsystem(
    val io: ElevatorIO,
) : Subsystem {
    private val ioInputs: ElevatorIO.ElevatorIOInputs = ElevatorIO.ElevatorIOInputs()

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
        get() = Config.ELEVATOR_STAGES[desiredStage] - io.motorEncoder

    override fun periodic() {
        io.updateInputs(ioInputs, currentStage)
        Logger.processInputs("Elevator", ioInputs)

        currentStage = Config.ELEVATOR_STAGES.indexOfFirst { it > io.motorEncoder }

        if (io.bottomLimitSwitchTriggered) {
            stopElevator()
            resetEncoder()
        }
    }


    /**
     * Runs the elevator at [speed]
     */
    fun runElevator(speed: Double) = io.runElevator(speed)

    /**
     * Runs the elevator motor up.
     */
    fun raiseElevator(speed: Double) = runElevator(1.0 * speed)

    /**
     * Runs the elevator motor down.
     */
    fun lowerElevator(speed: Double) = runElevator(-1.0 * speed)

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
