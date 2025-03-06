package org.frc5183.constants

class ConfigTest {
  fun `CORAL_MOTOR_MAXIMUM_SPEED is between -1 and 1`() {
    assert(Config.CORAL_MOTOR_MAXIMUM_SPEED in -1.0..1.0)
  }
}
