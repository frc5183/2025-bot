package org.frc5183.commands.coral

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.coral.CoralSubsystem

class IntakeCoralCommand(private val coralSubsystem: CoralSubsystem) : Command() {
    init {
        addRequirements(coralSubsystem)
    }

    override fun initialize() {
        coralSubsystem.runMotor(0.5)
    }

    override fun end(interrupted: Boolean) {
        coralSubsystem.stopMotor()
    }

    override fun isFinished(): Boolean = coralSubsystem.hasCoral
}
