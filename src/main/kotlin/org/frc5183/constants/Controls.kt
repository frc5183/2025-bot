package org.frc5183.constants

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Time
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.commands.drive.AimCommand
import org.frc5183.commands.drive.TeleopDriveCommand
import org.frc5183.commands.teleop.AutoAimAndShoot
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.target.FieldTarget

/**
 * An object representing the controls of the robot.
 */
object Controls {
    val DRIVER = CommandXboxController(0)
    val OPERATOR = CommandXboxController(1)

    val xSpeed: Double
        get() = DRIVER.leftX
    val ySpeed: Double
        get() = DRIVER.leftY
    val rotation: Double
        get() = DRIVER.rightX

    lateinit var TELEOP_DRIVE_COMMAND: Command

    val BUTTON_DEBOUNCE_TIME: Time = Units.Seconds.of(1.0)
    val CONTROLS_EVENT_LOOP = EventLoop()

    /**
     * Initializes the teleoperated command bindings and schedules the teleop drive command.
     *
     * If the teleop drive command is not yet initialized, it is created using the provided drive subsystem.
     * The function then sets up the following driver controller actions:
     * - D-PAD Up: Triggers an aim command targeting the pipe.
     * - X Button: Prints a debug message and schedules an auto-aim and shoot command.
     * - B Button: Cancels all current commands and reschedules the teleop drive command.
     *
     * Finally, the teleop drive command is scheduled for execution.
     *
     * @param drive the drive subsystem used for teleoperated driving.
     * @param vision the vision subsystem used for target acquisition.
     */
    fun teleopInit(
        drive: SwerveDriveSubsystem,
        vision: VisionSubsystem,
    ) {
        if (!this::TELEOP_DRIVE_COMMAND.isInitialized) {
            TELEOP_DRIVE_COMMAND = TeleopDriveCommand(drive)
        }

        // D-PAD Up
        DRIVER.povUp().debounce(BUTTON_DEBOUNCE_TIME.`in`(Units.Seconds)).onTrue(AimCommand(FieldTarget.Pipe, drive, vision))

        DRIVER.x().debounce(2.0).onTrue(
            InstantCommand({
                println("DEBUG")
                AutoAimAndShoot({ DRIVER.x().asBoolean }, drive, vision).schedule()
            }),
        )

        DRIVER.b().debounce(2.0).onTrue(
            InstantCommand({
                CommandScheduler.getInstance().cancelAll()
                TELEOP_DRIVE_COMMAND.schedule()
            }),
        )

        TELEOP_DRIVE_COMMAND.schedule()
    }

    /**
     * A function to be called during teleop periodic.
     * Mainly for polling the [CONTROLS_EVENT_LOOP].
     */
    fun teleopPeriodic() {
        CONTROLS_EVENT_LOOP.poll()
    }
}
