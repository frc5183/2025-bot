package org.frc5183.commands.climber

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.climber.ClimberSubsystem
import org.frc5183.math.curve.Curve

/**
 * Gives full control over the climber motor to an input lambda.
 */
class DriveClimberCommand(
    val climber: ClimberSubsystem,
    val input: () -> Double,
    val inputCurve: Curve,
) : Command() {
    init {
        addRequirements(climber)
    }

    override fun execute() {
        climber.runMotor(inputCurve(input()))
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }
}
