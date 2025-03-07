package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.elevator.ElevatorSubsystem
import org.frc5183.constants.Config

/**
 * Holds the elevator at its current position by correcting for
 * it slowly falling down.
 */
class HoldElevatorCommand(val elevator: ElevatorSubsystem) : Command() {
    init {
        addRequirements(elevator)
    }

    override fun execute() {
        if (elevator.stageDrift > Config.ELEVATOR_MAX_ALLOWED_DRIFT) {
            elevator.raiseElevator()
        } else {
            elevator.stopElevator()
        }
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }
}
