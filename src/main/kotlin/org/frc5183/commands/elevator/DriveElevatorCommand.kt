package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.math.curve.Curve
import org.frc5183.subsystems.elevator.ElevatorSubsystem

class DriveElevatorCommand(
    val elevator: ElevatorSubsystem,
    val input: () -> Double,
    val inputCurve: Curve,
) : Command() {
    init {
        addRequirements(elevator)
    }

    override fun execute() {
        val speed = inputCurve(input())

        if (elevator.speedMovesDown(speed) && elevator.bottomLimitSwitch) {
            elevator.stopElevator()
        } else if (elevator.speedMovesUp(speed) && elevator.topLimitSwitch) {
            elevator.stopElevator()
        } else {
            elevator.runElevator(speed)
        }
    }

    override fun isFinished() = false
}
