package org.frc5183.subsystems.coral.io

import com.revrobotics.ColorSensorV3
import com.revrobotics.spark.SparkMax
import org.frc5183.constants.Config

class RealCoralIO(private val motor: SparkMax, private val colorSensor: ColorSensorV3) : CoralIO {
    private val motorSpeed: Double
        get() = motor.get()

    override val proximityValue: Int
        get() = colorSensor.getProximity()

    override val proximityBaseline: Int = colorSensor.getProximity()
    
    override fun updateInputs(inputs: CoralIO.CoralIOInputs, hasCoral: Boolean) {
        inputs.hasCoral = hasCoral
        inputs.seesCoral = seesCoral
        inputs.motorSpeed = motorSpeed
        inputs.proximityValue = proximityValue
        inputs.proximityBaseline = proximityBaseline
    }

    override fun runMotor() {
        motor.set(Config.CORAL_MOTOR_MAXIMUM_SPEED)
    }

    override fun stopMotor() {
        motor.stopMotor()
    }
}
