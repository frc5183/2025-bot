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
     * A function to be called during teleop init.
     * Registers all the buttons to their respective commands and the drive command.
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
