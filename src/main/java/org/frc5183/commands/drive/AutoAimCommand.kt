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
    /**
     * Whether are not there are no targets in view.
     */
    private var noVisibleTargets: Boolean = false

    init {
        addRequirements(VisionSubsystem)
    }

    override fun initialize() {}

    override fun execute() {
        if (VisionSubsystem.visibleTargets.isEmpty()) {
            noVisibleTargets = true
        } else {
            for (camera in VisionSubsystem.cameras) {
                val target = VisionSubsystem.getNearestTarget(camera) ?: continue
                AimCommand(target).schedule()
                return
            }

            noVisibleTargets = true
        }
    }

    override fun isFinished(): Boolean = !noVisibleTargets

    override fun end(interrupted: Boolean) {}
}
