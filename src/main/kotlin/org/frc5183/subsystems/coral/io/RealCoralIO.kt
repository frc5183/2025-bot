package org.frc5183.subsystems.coral.io

import com.revrobotics.ColorSensorV3
import com.revrobotics.spark.SparkMax

class RealCoralIO(
    private val motor: SparkMax,
    private val colorSensor: ColorSensorV3,
) : CoralIO {
    private val motorSpeed: Double
        get() = motor.get()

    override val proximityValue: Int
        get() = colorSensor.getProximity()

    override fun updateInputs(
        inputs: CoralIO.CoralIOInputs,
        hasCoral: Boolean,
    ) {
        inputs.hasCoral = hasCoral
        inputs.seesCoral = seesCoral
        inputs.motorSpeed = motorSpeed
        inputs.proximityValue = proximityValue
    }

    override fun runMotor(speed: Double) {
        motor.set(speed)
    }

    override fun stopMotor() {
        motor.stopMotor()
    }
}
