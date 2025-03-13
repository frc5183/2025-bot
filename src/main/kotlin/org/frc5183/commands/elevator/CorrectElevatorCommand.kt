package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.elevator.ElevatorSubsystem
import org.frc5183.constants.Config

/**
 * Can be ran before another command to move the elevator to the correct
 * stage to account for sag.
 */
class CorrectElevatorCommand(val elevator: ElevatorSubsystem) : Command() {
    init {
        addRequirements(elevator)
    }

    override fun execute() {
        elevator.raiseElevator(Config.ELEVATOR_HOLD_SPEED)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }

    override fun isFinished(): Boolean {
        return elevator.stageDrift < Config.ELEVATOR_MAX_ALLOWED_DRIFT
    }
}

