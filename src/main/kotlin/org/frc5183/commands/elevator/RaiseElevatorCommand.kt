package org.frc5183.commands.elevator

import org.frc5183.subsystems.elevator.ElevatorSubsystem
import edu.wpi.first.wpilibj2.command.Command

class RaiseElevatorCommand(val elevator: ElevatorSubsystem) : Command() {
    private var desiredStage: Int = -1 // lateinit primitives are annoying.

    init {
        addRequirements(elevator)
    }

    override fun initialize() {
      desiredStage = elevator.currentStage + 1
    }

    override fun execute() {
        elevator.raiseElevator()
    }

    override fun isFinished() = desiredStage <= elevator.currentStage
}
