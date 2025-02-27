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

    /**
     * Resets the command's cancelled state to indicate it is active.
     *
     * This method is called when the command is initialized.
     */
    override fun initialize() {
        cancelled = false
    }

    /**
     * Executes a single iteration of teleoperated driving.
     *
     * This method calculates the robot's translation and rotation speeds by scaling the control inputs
     * with the maximum allowable speeds defined in [PhysicalConstants]. It converts these speeds from
     * robot-relative to field-relative coordinates using the robot's current rotation (from the drive
     * subsystem's pose) and passes the resulting [ChassisSpeeds] to the [SwerveDriveSubsystem].
     *
     * Future improvements may include the implementation of acceleration curves.
     */
    override fun execute() {
        // todo add acceleration curves and all of that

        val maxTranslationMPS = PhysicalConstants.MAX_SPEED.`in`(Units.MetersPerSecond) * 0.9
        val xSpeed = Controls.xSpeed * maxTranslationMPS
        val ySpeed = Controls.ySpeed * maxTranslationMPS

        val maxRotationRPS = PhysicalConstants.MAX_ANGULAR_VELOCITY.`in`(Units.RotationsPerSecond)
        val rotationSpeed = Controls.rotation * maxRotationRPS

        drive.drive(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotationSpeed, drive.pose.rotation))
    }

    /**
 * Determines if the command should finish execution.
 *
 * The command is complete if teleoperated mode is disabled or if it has been cancelled.
 *
 * @return true if teleop mode is turned off or the command has been cancelled, false otherwise.
 */
override fun isFinished(): Boolean = !Robot.isTeleopEnabled || cancelled

    override fun end(interrupted: Boolean) {
        if (!Robot.isTeleopEnabled && interrupted) {
            schedule()
        }
        cancelled = true
    }
}
