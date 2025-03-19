package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

/**
 * Holds the elevator at its current position by correcting for
 * it slowly falling down.
 */
class HoldElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    init {
        addRequirements(elevator)
    }

    override fun execute() {
        elevator.raiseElevator(Config.ELEVATOR_HOLD_SPEED)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }
}
