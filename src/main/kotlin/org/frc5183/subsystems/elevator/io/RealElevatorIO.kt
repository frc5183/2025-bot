package org.frc5183.subsystems.elevator.io

import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Alert
import org.frc5183.constants.Config

class RealElevatorIO(
    private val motor: SparkMax,
    private val bottomLimitSwitch: DigitalInput,
) : ElevatorIO {
    override val motorSpeed: Double
        get() = motor.get()

    override val motorEncoder: Angle
        get() = Units.Rotations.of((if (Config.ELEVATOR_MOTOR_INVERTED) -1.0 else 1.0) * motor.encoder.position)

    override val bottomLimitSwitchTriggered: Boolean
        get() = !bottomLimitSwitch.get()

    init {
        if (bottomLimitSwitchTriggered)
          motor.encoder.position = 0.0
        else
          Alert("Bottom limit switch was not triggered at startup!", Alert.AlertType.kError).set(true)
    }

    override fun updateInputs(
        inputs: ElevatorIO.ElevatorIOInputs,
        currentStage: Int,
    ) {
        inputs.motorEncoder = motorEncoder.`in`(Units.Rotations)
        inputs.currentStage = currentStage
        inputs.bottomLimitSwitchTriggered = bottomLimitSwitchTriggered
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
