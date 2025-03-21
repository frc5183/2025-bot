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
        climber.runMotor(-0.5)
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }

    override fun isFinished(): Boolean = climber.limitSwitch
}
