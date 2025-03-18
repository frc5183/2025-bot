package org.frc5183.subsystems.coral

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.subsystems.coral.io.CoralIO
import org.frc5183.subsystems.elevator.ElevatorSubsystem
import org.littletonrobotics.junction.Logger

class CoralSubsystem(private val io: CoralIO, private val elevator: ElevatorSubsystem) : Subsystem {
    private val ioInputs = CoralIO.CoralIOInputs()

    var hasCoral: Boolean = false
        private set

    val seesCoral: Boolean
        get() = io.seesCoral

    private var visibleCoralBuffer: Boolean = false

    override fun periodic() {
      io.updateInputs(ioInputs, hasCoral)
      SmartDashboard.putBoolean("Has Coral", hasCoral)
      SmartDashboard.putBoolean("Sees Coral", seesCoral)
      Logger.processInputs("Coral", ioInputs)

      if (!elevator.bottomLimitSwitch) return // Ignore any coral stuff when moving elevator.
      if (seesCoral && !visibleCoralBuffer) visibleCoralBuffer = true
      if (!seesCoral && visibleCoralBuffer) {
        hasCoral = true
        visibleCoralBuffer = false
      }
    }

    fun runMotor(speed: Double) {
        io.runMotor(speed)
    }

    fun stopMotor() {
        io.stopMotor()
    }

    fun clearCoral() {
        hasCoral = false
        visibleCoralBuffer = false
    }
}
