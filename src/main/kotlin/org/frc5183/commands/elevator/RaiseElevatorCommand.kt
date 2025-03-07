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
        if (elevator.currentStage >= Config.ELEVATOR_STAGES.size) {
            invalidStage = true
            return
        }
        elevator.desiredStage += 1

        elevator.raiseElevator(0.5)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }


    override fun isFinished() = elevator.desiredStage <= elevator.currentStage || invalidStage
}
