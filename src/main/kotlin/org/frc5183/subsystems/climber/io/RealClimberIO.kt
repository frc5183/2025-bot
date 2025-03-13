package org.frc5183.subsystems.climber.io

import com.revrobotics.spark.SparkMax

class RealClimberIO(
    val motor: SparkMax,
) : ClimberIO {
    override fun updateInputs(inputs: ClimberIO.ClimberIOInputs) {
        inputs.motorSpeed = motor.get()
    }

    override fun runMotor(speed: Double) {
        motor.set(speed)
    }

    override fun stopMotor() {
        motor.stopMotor()
    }
}
