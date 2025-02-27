package org.frc5183.commands.drive

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem

/**
 * Aims the robot at the closest target.
 * If multiple targets are in view on multiple cameras,
 * the robot will aim at the closest one on the primary camera.
 */
// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
// todo v2: is there a better way to do these kinds of commands? idk if subclassing command is the right way or not
fun AutoAimCommand(drive: SwerveDriveSubsystem, vision: VisionSubsystem): Command {
    vision.getNearestTarget()?.let { target ->
        return AimCommand(target, drive, vision)
    }
    return InstantCommand()
}
/*
class AutoAimCommand(
    val drive: SwerveDriveSubsystem,
    val vision: VisionSubsystem,
) : Command() {
    private lateinit var aimCommand: AimCommand
    private var finished: Boolean = false

    init {
        addRequirements(vision)
    }

    override fun initialize() {
        if (vision.visibleTargets.isEmpty()) {
            finished = true
        } else {
            vision.getNearestTarget()?.let { target ->
                aimCommand = AimCommand(target, drive, vision)
            }

            finished = true
        }
    }

    override fun execute() {
        if (!aimCommand.isScheduled) {
            aimCommand.schedule()
        }
    }

    override fun isFinished(): Boolean = finished

    override fun end(interrupted: Boolean) {
        finished = true
    }
}

 */