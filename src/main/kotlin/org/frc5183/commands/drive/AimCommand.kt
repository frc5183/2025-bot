package org.frc5183.commands.drive

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.target.FieldTarget
import org.littletonrobotics.junction.AutoLog

/**
 * Aims the robot at the closest [target] if it is in view.
 * If multiple targets are in view on multiple cameras,
 * the robot will aim at the closest one on the primary camera.
 */
// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
@AutoLog
class AimCommand(
    private val target: FieldTarget,
    private val drive: SwerveDriveSubsystem,
    private val vision: VisionSubsystem,
) : Command() {
    private var command: Command? = null
    private var finished = false

    init {
        addRequirements(drive)
        addRequirements(vision)
    }

    override fun initialize() {
        val pose = vision.getAimPose(target)
        if (pose == null) {
            finished = true
            return
        }

        command = DriveToPose2d(pose, drive, vision)
    }

    override fun execute() {
        command?.schedule()
        finished = true
    }

    override fun isFinished(): Boolean = finished

    override fun end(interrupted: Boolean) {
        finished = true
    }
}
