package org.frc5183.subsystems.coral.io

import org.frc5183.constants.Config
import org.frc5183.log.AutoLogInputs

interface CoralIO {
    class CoralIOInputs : AutoLogInputs() {
        var hasCoral by log(false)
        var seesCoral by log(false)
        var motorSpeed by log(0.0)
        var proximityValue by log(0)
    }

    val seesCoral: Boolean
        get() = proximityValue >= Config.CORAL_PROXIMITY_THRESHOLD

    val proximityValue: Int

    fun updateInputs(
        inputs: CoralIOInputs,
        hasCoral: Boolean,
    )

    fun runMotor(speed: Double)

    fun stopMotor()
}
