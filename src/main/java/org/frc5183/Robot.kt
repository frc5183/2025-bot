package org.frc5183

import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.pathfinding.Pathfinding
import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.frc5183.commands.drive.DriveToPose2d
import org.frc5183.constants.Config
import org.frc5183.constants.Controls
import org.frc5183.subsystems.SwerveDriveSubsystem
import org.frc5183.subsystems.VisionSubsystem

/**
 * The functions in this object (which basically functions as a singleton class) are called automatically
 * corresponding to each mode, as described in the TimedRobot documentation.
 * This is written as an object rather than a class since there should only ever be a single instance, and
 * it cannot take any constructor arguments. This makes it a natural fit to be an object in Kotlin.
 *
 * If you change the name of this object or its package after creating this project, you must also update
 * the `Main.kt` file in the project. (If you use the IDE's Rename or Move refactorings when renaming the
 * object or package, it will get changed everywhere.)
 */
object Robot : TimedRobot() {
    // Todo: move?
    val field: Field2d = Field2d()

    val simulation: Boolean
        get() = isSimulation()

    val brakeTimer = Timer()

    private var selectedAutoMode = AutoMode.default
    private val autoModeChooser =
        SendableChooser<AutoMode>().also { chooser ->
            AutoMode.entries.forEach { chooser.addOption(it.optionName, it) }
            chooser.setDefaultOption(AutoMode.default.optionName, AutoMode.default)
        }

    /**
     * A enumeration of the available autonomous modes.
     *
     * @param optionName The name for the [autoModeChooser] option.
     * @param periodicFunction The function that is called in the [autonomousPeriodic] function each time it is called.
     * @param autoInitFunction An optional function that is called in the [autonomousInit] function.
     */
    private enum class AutoMode(
        val optionName: String,
        val periodicFunction: () -> Unit,
        val autoInitFunction: () -> Unit = { /* No op by default */ },
    ) {
        CUSTOM_AUTO_1("Custom Auto Mode 1", ::autoMode1),
        CUSTOM_AUTO_2("Custom Auto Mode 2", ::autoMode2),
        ;

        companion object {
            /** The default auto mode. */
            val default = CUSTOM_AUTO_1
        }
    }

    init
    {
        // Report our language as Kotlin and not Java.
        HAL.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Kotlin, 0, WPILibVersion.Version)
        SmartDashboard.putData("Auto choices", autoModeChooser)
        SmartDashboard.putData("Field", field)

        CommandScheduler.getInstance().registerSubsystem(
            SwerveDriveSubsystem,
            VisionSubsystem,
        )

        // todo debug sets the pose2d to into the field in sim
        //SwerveDriveSubsystem.resetPose(Pose2d(3.0, 2.0, Rotation2d(0.0, 0.0)))

        // Set the pathfinder to use the LocalADStarAK pathfinder so that
        //  we can use the AdvantageKit replay logging.
        Pathfinding.setPathfinder(LocalADStarAK())

        CommandScheduler.getInstance().onCommandInitialize {
            println("Command initialized: ${it.name}")
        }

        CommandScheduler.getInstance().onCommandExecute {
            if (it.name != "TeleopDriveCommand") {
                println("Command executed: ${it.name}")
            }
        }

        CommandScheduler.getInstance().onCommandFinish {
            println("Command finished: ${it.name}")
        }

        CommandScheduler.getInstance().onCommandInterrupt { it: Command ->
            println("Command interrupted: ${it.name}")
        }
    }

    override fun robotPeriodic() {
        field.robotPose = SwerveDriveSubsystem.pose
        CommandScheduler.getInstance().run()
    }

    override fun autonomousInit() {
        CommandScheduler.getInstance().cancelAll()
        selectedAutoMode = autoModeChooser.selected ?: AutoMode.default
        println("Selected auto mode: ${selectedAutoMode.optionName}")
        selectedAutoMode.autoInitFunction.invoke()
    }

    override fun autonomousPeriodic() = selectedAutoMode.periodicFunction.invoke()

    private fun autoMode1() {
        DriveToPose2d(Pose2d(15.0, 3.0, Rotation2d(0.0, 0.0))).schedule()
        /*
        val pose = Pose2d(15.0, 3.0, Rotation2d(0.0, 0.0))

        AutoBuilder
            .pathfindThenFollowPath(
                PathPlannerPath(
                    PathPlannerPath.waypointsFromPoses(
                        Pose2d(SwerveDriveSubsystem.pose.translation, Rotation2d.kZero),
                        Pose2d(pose.translation, Rotation2d.kZero),
                    ),
                    AutoConstants.PATH_CONSTRAINTS,
                    null,
                    GoalEndState(0.0, pose.rotation),
                ),
                AutoConstants.PATH_CONSTRAINTS,
            ).schedule()
         */
    }

    private fun autoMode2() {
        PathPlannerAuto("Auto 2").schedule()
    }

    /** This method is called once when teleop is enabled.  */
    override fun teleopInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.teleopInit() // Register all teleop controls.
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {
        Controls.teleopPeriodic()
    }

    /** This method is called once when the robot is disabled.  */
    override fun disabledInit() {
        CommandScheduler.getInstance().cancelAll()
        SwerveDriveSubsystem.setMotorBrake(true)
        brakeTimer.reset()
        brakeTimer.start()
    }

    /** This method is called periodically when disabled.  */
    override fun disabledPeriodic() {
        if (brakeTimer.advanceIfElapsed(Config.BREAK_TIME_AFTER_DISABLE.inWholeSeconds.toDouble())) {
            SwerveDriveSubsystem.setMotorBrake(false)
            brakeTimer.stop()
            brakeTimer.reset()
        }
    }

    /** This method is called once when test mode is enabled.  */
    override fun testInit() {}

    /** This method is called periodically during test mode.  */
    override fun testPeriodic() {}

    /** This method is called once when the robot is first started up.  */
    override fun simulationInit() {}

    /** This method is called periodically whilst in simulation.  */
    override fun simulationPeriodic() {}
}
