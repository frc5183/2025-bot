package org.frc5183.commands.drive

import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.Robot
import org.frc5183.constants.Controls
import org.frc5183.constants.PhysicalConstants
import org.frc5183.subsystems.SwerveDriveSubsystem

class TeleopDriveCommand : Command() {
    init {
        // each subsystem used by the command must be passed into the addRequirements() method
        addRequirements(SwerveDriveSubsystem)
    }

    override fun execute() {
        val maxTranslationMPS = PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond) * 0.9
        val xSpeed = Controls.xSpeed * maxTranslationMPS
        val ySpeed = Controls.ySpeed * maxTranslationMPS

        val maxRotationRPS = PhysicalConstants.MAX_ANGULAR_VELOCITY.`in`(Units.RadiansPerSecond) * 0.01
        val rotationSpeed = Controls.rotation * maxRotationRPS

        SwerveDriveSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, 0.0, SwerveDriveSubsystem.pose.rotation))
    }

    override fun isFinished(): Boolean = !Robot.isTeleopEnabled
}
