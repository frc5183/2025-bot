package org.frc5183.subsystems.climber.io

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.DigitalInput

class RealClimberIO(
    val motor: SparkMax,
    val limitSwitch: DigitalInput,
) : ClimberIO {
    override val limitSwitchTriggered: Boolean
        get() = limitSwitch.get()

    override fun updateInputs(inputs: ClimberIO.ClimberIOInputs) {
        inputs.motorSpeed = motor.get()
        inputs.limitSwitch = limitSwitchTriggered
    }

    override fun runMotor(speed: Double) {
        motor.set(speed)
    }

    override fun stopMotor() {
        motor.stopMotor()
    }
}
