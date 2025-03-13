package org.frc5183.subsystems.climber.io

import org.frc5183.log.AutoLogInputs

interface ClimberIO {
    class ClimberIOInputs : AutoLogInputs() {
        var motorSpeed by log(0.0)
    }

    /**
     * Update the [inputs] to be logged.
     * @param inputs The inputs to log.
     */
    fun updateInputs(inputs: ClimberIOInputs)

    /**
     * Run the motor at the given [speed].
     * @param speed The speed to run the motor at, from [-1, 1].
     */
    fun runMotor(speed: Double)

    /**
     * Stop the motor.
     */
    fun stopMotor()
}
