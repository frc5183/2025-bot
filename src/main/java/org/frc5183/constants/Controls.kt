package org.frc5183.constants

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.frc5183.commands.drive.AimCommand
import org.frc5183.commands.drive.AutoAimCommand
import org.frc5183.target.FieldTarget

/**
 * An object representing the controls of the robot.
 */
object Controls {
    val DRIVER = XboxController(0)
    val OPERATOR = XboxController(1)

    val xSpeed: Double
        get() = DRIVER.leftX
    val ySpeed: Double
        get() = DRIVER.leftY
    val rotation: Double
        get() = DRIVER.rightX

    fun registerControls() {
        // D-PAD Up
        DRIVER.povUp(EventLoop()).ifHigh {
            CommandScheduler.getInstance().schedule(AimCommand(FieldTarget.Pipe))
        }

        DRIVER.x(EventLoop()).ifHigh {
            CommandScheduler.getInstance().schedule(AutoAimCommand())
        }

        DRIVER.b(EventLoop()).ifHigh {
            CommandScheduler.getInstance().cancelAll()
        }
    }
}
