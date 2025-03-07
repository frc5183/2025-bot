package org.frc5183.subsystems.elevator.io

import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.wpilibj.DigitalInput

class RealElevatorIO(
    private val motor: SparkMax,
    private val bottomLimitSwitch: DigitalInput,
) : ElevatorIO {
    override val motorEncoder: Angle
        get() = Units.Rotations.of(motor.encoder.position)

    override val bottomLimitSwitchTriggered: Boolean
        get() = bottomLimitSwitch.get()

    override fun updateInputs(
        inputs: ElevatorIO.ElevatorIOInputs,
        currentStage: Int,
    ) {
        inputs.motorEncoder = motorEncoder.`in`(Units.Rotations)
        inputs.currentStage = currentStage
        inputs.bottomLimitSwitchTriggered = bottomLimitSwitch.get()
    }

    override fun runElevator(speed: Double) {
        motor.set(speed)
    }

    override fun stopElevator() {
        motor.set(0.0)
        motor.stopMotor()
    }

    override fun resetEncoder() {
        motor.encoder.setPosition(0.0)
    }
}
