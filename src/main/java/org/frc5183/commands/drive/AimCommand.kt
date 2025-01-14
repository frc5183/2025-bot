package org.frc5183.commands.drive

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.SwerveDriveSubsystem
import org.frc5183.subsystems.VisionSubsystem
import org.frc5183.target.FieldTarget

/**
 * Aims the robot at the closest [target] if it is in view.
 * If multiple targets are in view on multiple cameras,
 * the robot will aim at the closest one on the primary camera.
 */
// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
class AimCommand(
    val target: FieldTarget,
) : Command() {
    private lateinit var pose: Pose2d

    init {
        addRequirements(SwerveDriveSubsystem)
        addRequirements(VisionSubsystem)
    }

    override fun initialize() {
        val pose = VisionSubsystem.getAimPose(target)
    }

    override fun execute() {
    }

    override fun isFinished(): Boolean = false

    override fun end(interrupted: Boolean) {}
}
