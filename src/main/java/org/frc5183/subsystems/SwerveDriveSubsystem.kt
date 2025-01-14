package org.frc5183.subsystems

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathfindingCommand
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.Robot
import org.frc5183.constants.AutoConstants
import org.frc5183.constants.DeviceConstants
import org.frc5183.constants.LoggingConstants
import org.frc5183.constants.PhysicalConstants
import org.frc5183.constants.swerve.SwerveConstants
import swervelib.SwerveDrive
import swervelib.telemetry.SwerveDriveTelemetry
import kotlin.jvm.optionals.getOrNull

object SwerveDriveSubsystem : Subsystem {
    private val drive: SwerveDrive = SwerveConstants.SWERVE_DRIVE

    /**
     * The current [Pose2d] of the robot according to kinematics and vision (if enabled).
     */
    val pose: Pose2d get() = drive.pose

    /**
     * The current [ChassisSpeeds] of the robot relative to the robot.
     */
    val robotVelocity: ChassisSpeeds get() = drive.robotVelocity

    init {
        SwerveDriveTelemetry.verbosity = LoggingConstants.SWERVE_VERBOSITY

        if (Robot.simulation) {
            drive.setHeadingCorrection(false)
            drive.setCosineCompensator(false)
        }

        AutoBuilder.configure(
            { pose },
            { pose: Pose2d -> resetPose(pose) },
            { robotVelocity },
            { speeds: ChassisSpeeds -> drive(speeds) },
            PPHolonomicDriveController(
                PhysicalConstants.TRANSLATION_PID_CONSTANTS,
                PhysicalConstants.ROTATION_PID_CONSTANTS,
            ),
            AutoConstants.ROBOT_CONFIG,
            { DriverStation.getAlliance().getOrNull() == Alliance.Red },
            this,
            VisionSubsystem,
        )

        // Warm up the pathfinding command
        // https://pathplanner.dev/pplib-follow-a-single-path.html#java-warmup
        PathfindingCommand.warmupCommand().schedule()

        if (DeviceConstants.DRIVE_VISION && VisionSubsystem.cameras.isNotEmpty()) {
            //drive.stopOdometryThread()
        }
    }

    override fun periodic() {
        if (DeviceConstants.DRIVE_VISION && VisionSubsystem.cameras.isNotEmpty()) {
            //drive.updateOdometry()
            //updatePoseEstimationWithVision()
        } else {
            //drive.updateOdometry()
        }
    }

    private fun updatePoseEstimationWithVision() {
        TODO("Not yet implemented")
    }

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean) = drive.setMotorIdleMode(brake)

    /**
     * Resets the robot's pose to [Pose2d.kZero].
     */
    fun resetPose() = resetPose(Pose2d.kZero)

    /**
     * Resets the robot's pose to [pose].
     */
    fun resetPose(pose: Pose2d) = drive.resetOdometry(pose)

    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldRelative: Boolean,
        isOpenLoop: Boolean,
    ) = drive.drive(translation, rotation, fieldRelative, isOpenLoop)

    fun drive(speeds: ChassisSpeeds) = drive.drive(speeds)
}
