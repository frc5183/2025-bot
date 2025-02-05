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
import org.frc5183.target.FieldTarget

/**
 * An object representing the controls of the robot.
 */
object Controls {
    val DRIVER = CommandXboxController(0)
    val OPERATOR = CommandXboxController(1)

    val xSpeed: Double
        get() = DRIVER.leftY
    val ySpeed: Double
        get() = DRIVER.leftX
    val rotation: Double
        get() = DRIVER.rightX

    val TELEOP_DRIVE_COMMAND: Command = TeleopDriveCommand()

    val BUTTON_DEBOUNCE_TIME: Time = Units.Seconds.of(1.0)
    val CONTROLS_EVENT_LOOP = EventLoop()

    /**
     * A function to be called during teleop init.
     * Registers all the buttons to their respective commands and the drive command.
     */
    fun teleopInit() {
        // D-PAD Up
        DRIVER.povUp().debounce(BUTTON_DEBOUNCE_TIME.`in`(Units.Seconds)).onTrue(AimCommand(FieldTarget.Pipe))

        DRIVER.x().debounce(2.0).onTrue(
            InstantCommand({
                println("DEBUG")
                AutoAimAndShoot { DRIVER.x().asBoolean }.schedule()
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
