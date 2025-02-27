package org.frc5183.commands.drive

import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.Robot
import org.frc5183.constants.Controls
import org.frc5183.constants.PhysicalConstants
import org.frc5183.subsystems.drive.SwerveDriveSubsystem

class TeleopDriveCommand(
    val drive: SwerveDriveSubsystem,
) : Command() {
    private var cancelled = false

    init {
        // each subsystem used by the command must be passed into the addRequirements() method
        addRequirements(drive)
    }

    override fun initialize() {
        cancelled = false
    }

    override fun execute() {
        // todo add acceleration curves and all of that

        val maxTranslationMPS = PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond) * 0.9
        val xSpeed = Controls.xSpeed * maxTranslationMPS
        val ySpeed = Controls.ySpeed * maxTranslationMPS

        val maxRotationRPS = PhysicalConstants.MAX_ANGULAR_VELOCITY.`in`(Units.RotationsPerSecond)
        val rotationSpeed = Controls.rotation * maxRotationRPS

        drive.drive(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotationSpeed, drive.pose.rotation))
    }

    override fun isFinished(): Boolean = !Robot.isTeleopEnabled || cancelled

    override fun end(interrupted: Boolean) {
        if (!Robot.isTeleopEnabled && interrupted) {
            schedule()
        }
        cancelled = true
    }
}
