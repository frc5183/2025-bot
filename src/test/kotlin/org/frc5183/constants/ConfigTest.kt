package org.frc5183.constants

class ConfigTest {
  fun `CORAL_MOTOR_MAXIMUM_SPEED is between -1 and 1`() {
    assert(Config.CORAL_MOTOR_MAXIMUM_SPEED in -1.0..1.0)
  }

  fun `CORAL_MOTOR_INTAKE_DIRECTION is -1 or 1`() {
    assert(Config.CORAL_MOTOR_INTAKE_DIRECTION == -1.0 || Config.CORAL_MOTOR_INTAKE_DIRECTION == 1.0)
  }

  fun `CORAL_MOTOR_SHOOT_DIRECTION is -1 or 1`() {
    assert(Config.CORAL_MOTOR_SHOOT_DIRECTION == -1.0 || Config.CORAL_MOTOR_SHOOT_DIRECTION == 1.0)
  }

  fun `CORAL_MOTOR_SHOOT_DIRECTION and CORAL_MOTOR_INTAKE_DIRECTION are not equal`() {
    assert(Config.CORAL_MOTOR_SHOOT_DIRECTION != Config.CORAL_MOTOR_INTAKE_DIRECTION)
  }
}
