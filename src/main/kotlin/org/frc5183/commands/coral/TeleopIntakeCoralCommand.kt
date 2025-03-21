package org.frc5183.commands.coral

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.coral.CoralSubsystem

class TeleopIntakeCoralCommand(
    private val coralSubsystem: CoralSubsystem,
) : Command() {
    init {
        addRequirements(coralSubsystem)
    }

    override fun initialize() {
        coralSubsystem.runMotor(Config.CORAL_INTAKE_SPEED)
    }

    override fun end(interrupted: Boolean) {
        coralSubsystem.stopMotor()
    }

    override fun isFinished(): Boolean = false
}
