package org.frc5183

import com.pathplanner.lib.pathfinding.Pathfinding
import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.frc5183.commands.drive.TeleopDriveCommand
import org.frc5183.constants.Config
import org.frc5183.constants.Controls
import org.frc5183.subsystems.SwerveDriveSubsystem

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
            AutoMode.values().forEach { chooser.addOption(it.optionName, it) }
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

        // Set the pathfinder to use the LocalADStarAK pathfinder so that
        //  we can use the AdvantageKit replay logging.
        Pathfinding.setPathfinder(LocalADStarAK())
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
        TODO("Write custom auto mode 1")
    }

    private fun autoMode2() {
        TODO("Write custom auto mode 2")
    }

    /** This method is called once when teleop is enabled.  */
    override fun teleopInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.registerControls() // Register all teleop controls.
        TeleopDriveCommand().schedule()

        // todo debug sets the pose2d to into the field in sim
        SwerveDriveSubsystem.resetPose(Pose2d(13.0, 2.0, Rotation2d(0.0 ,0.0)))
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {
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
