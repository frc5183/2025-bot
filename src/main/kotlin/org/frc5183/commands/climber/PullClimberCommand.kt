package org.frc5183.commands.climber

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.climber.ClimberSubsystem

/**
 * Drives the climber backwards, pulling in the cage.
 */
class PullClimberCommand(
    val climber: ClimberSubsystem,
) : Command() {
    init {
        addRequirements(climber)
    }

    override fun execute() {
        climber.runMotor(1.0)
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }
}
