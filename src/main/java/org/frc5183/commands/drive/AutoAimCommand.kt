package org.frc5183.commands.drive

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.subsystems.VisionSubsystem

/**
 * Aims the robot at the closest target.
 * If multiple targets are in view on multiple cameras,
 * the robot will aim at the closest one on the primary camera.
 */
// todo: make sure this doesn't run during teleop if we're not holding a button otherwise its not allowed by FRC rules
class AutoAimCommand : Command() {
    private lateinit var aimCommand: AimCommand
    private var finished: Boolean = false

    init {
        addRequirements(VisionSubsystem)
    }

    override fun initialize() {
        if (VisionSubsystem.visibleTargets.isEmpty()) {
            finished = true
        } else {
            for (camera in VisionSubsystem.cameras) {
                aimCommand = AimCommand(VisionSubsystem.getNearestTarget(camera) ?: continue)
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
