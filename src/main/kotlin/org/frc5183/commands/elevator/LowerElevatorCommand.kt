package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.elevator.ElevatorSubsystem

class LowerElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    private var invalidStage: Boolean = false

    init {
        addRequirements(elevator)
    }

    override fun initialize() {
        if (elevator.currentStage <= 0) {
            invalidStage = true
            return
        }
        elevator.desiredStage = elevator.currentStage - 1
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }

    override fun isFinished() = elevator.currentStage <= elevator.desiredStage || invalidStage
}
