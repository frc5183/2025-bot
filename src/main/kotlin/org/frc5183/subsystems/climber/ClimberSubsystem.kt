package org.frc5183.subsystems.climber

import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.subsystems.climber.io.ClimberIO
import org.littletonrobotics.junction.Logger

class ClimberSubsystem(
    val io: ClimberIO,
) : Subsystem {
    val limitSwitch: Boolean
        get() = io.limitSwitchTriggered

    val ioInputs: ClimberIO.ClimberIOInputs = ClimberIO.ClimberIOInputs()

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Climber", ioInputs)

        if (io.limitSwitchTriggered && ioInputs.motorSpeed < 0) stopMotor()
    }

    /**
     * Run the motor at the given [speed].
     * @param speed The speed to run the motor at, from [-1, 1].
     */
    fun runMotor(speed: Double) {
        if (!io.limitSwitchTriggered) io.runMotor(speed)

        if (io.limitSwitchTriggered && speed > 0) io.runMotor(speed)
    }

    /**
     * Stop the motor.
     */
    fun stopMotor() = io.stopMotor()
}
