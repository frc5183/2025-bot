package org.frc5183.commands

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.climber.ClimberSubsystem

/**
 * Drives the climber in a given direction.
 */
class ClimbCommand(
    val climber: ClimberSubsystem,
    val direction: Direction,
) : Command() {
    enum class Direction {
        UP,
        DOWN,
    }

    init {
        addRequirements(climber)
    }

    override fun execute() {
        when (direction) {
            Direction.UP -> {
                climber.runMotor(1.0)
            }
            Direction.DOWN -> {
                climber.runMotor(-1.0)
            }
        }
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }
}
