package org.frc5183.commands.teleop

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.commands.drive.AutoAimCommand

class AutoAimAndShoot(
    private val buttonHeld: () -> Boolean,
) : Command() {
    private val commandGroup =
        SequentialCommandGroup(
            AutoAimCommand(),
            FunctionalCommand(
                { println("Shooting") },
                { println("Shooting") },
                { },
                { true },
            ),
        )

    private var finished = false

    override fun execute() {
        if (!commandGroup.isScheduled) commandGroup.schedule()

        if (commandGroup.isFinished) {
            finished = true
        }

        if (!buttonHeld.invoke()) {
            commandGroup.cancel()
            finished = true
        }
    }

    override fun isFinished(): Boolean = finished

    override fun end(interrupted: Boolean) {
        commandGroup.cancel()
        finished = true
    }
}
