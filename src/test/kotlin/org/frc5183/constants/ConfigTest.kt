package org.frc5183.constants

class ConfigTest {
    fun `ELEVATOR_MOVEMENT_SPEED is between -1 and 1`() {
        assert(Config.ELEVATOR_MOVEMENT_SPEED in -1.0..1.0)
    }

    fun `ELEVATOR_HOLD_SPEED is between -1 and 1`() {
        assert(Config.ELEVATOR_HOLD_SPEED in -1.0..1.0)
    }

    fun `CORAL_INTAKE_SPEED is between -1 and 1`() {
        assert(Config.CORAL_INTAKE_SPEED in -1.0..1.0)
    }

    fun `CORAL_SHOOT_SPEED is between -1 and 1`() {
        assert(Config.CORAL_SHOOT_SPEED in -1.0..1.0)
    }

    fun `CORAL_PROXIMITY_THRESHOLD is between 0 and 2047`() {
        assert(Config.CORAL_PROXIMITY_THRESHOLD in 0..2047)
    }
}
