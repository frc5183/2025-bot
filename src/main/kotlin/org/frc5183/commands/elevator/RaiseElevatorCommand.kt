package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

class RaiseElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    private var invalidStage: Boolean = false

    init {
        addRequirements(elevator)
    }

    override fun initialize() {
        if (elevator.desiredStage >= Config.ELEVATOR_STAGES.size) {
            invalidStage = true
            return
        }
        elevator.desiredStage += 1

        elevator.raiseElevator(Config.ELEVATOR_MOVEMENT_SPEED)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }


    override fun isFinished() = elevator.desiredStage <= elevator.currentStage || invalidStage
}
