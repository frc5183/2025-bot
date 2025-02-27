package org.frc5183.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathfindingCommand
import com.pathplanner.lib.config.PIDConstants
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.*
import org.frc5183.constants.swerve.SwervePIDConstants
import org.frc5183.subsystems.drive.io.SwerveDriveIO
import org.frc5183.subsystems.vision.VisionSubsystem
import org.littletonrobotics.junction.Logger
import kotlin.jvm.optionals.getOrNull

class SwerveDriveSubsystem(
    val io: SwerveDriveIO,
    val vision: VisionSubsystem? = null,
) : Subsystem {
    private val ioInputs: SwerveDriveIO.SwerveDriveIOInputs = SwerveDriveIO.SwerveDriveIOInputs()

    val pose: Pose2d
        get() = io.pose

    val robotVelocity: ChassisSpeeds
        get() = io.robotVelocity

    init {
        AutoBuilder.configure(
            { pose },
            io::resetPose,
            { robotVelocity },
            io::drive,
            PPHolonomicDriveController(
                PIDConstants(
                    SwervePIDConstants.DRIVE_PID.p,
                    SwervePIDConstants.DRIVE_PID.i,
                    SwervePIDConstants.DRIVE_PID.d,
                ),
                PIDConstants(
                    SwervePIDConstants.ANGLE_PID.p,
                    SwervePIDConstants.ANGLE_PID.i,
                    SwervePIDConstants.ANGLE_PID.d,
                ),
            ),
            AutoConstants.ROBOT_CONFIG,
            { DriverStation.getAlliance().getOrNull() == Alliance.Red },
            this,
            vision,
        )

        // https://pathplanner.dev/pplib-follow-a-single-path.html#java-warmup
        PathfindingCommand.warmupCommand().schedule()

        if (Config.VISION_POSE_ESTIMATION && vision != null) {
            io.stopOdometryThread()
        }
    }

    /**
     * Performs the periodic update for the swerve drive subsystem.
     *
     * This method refreshes the hardware input readings and logs them. If vision-based pose
     * estimation is enabled and a vision subsystem is provided, it also updates the drive odometry,
     * refines the robot's pose with vision data, and subsequently updates the robot pose.
     */
    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Swerve", ioInputs)

        if (Config.VISION_POSE_ESTIMATION && vision != null) {
            io.updateOdometry()
            updatePoseEstimationWithVision(vision)
            vision.updateRobotPose(pose)
        }
    }

    /**
     * Updates the robot's pose estimation using vision measurements.
     *
     * Iterates through each camera in the provided vision subsystem and, if an estimated pose is available,
     * converts the pose to a 2D representation and logs the measurement (with its timestamp and standard deviations)
     * to the drive I/O.
     *
     * @param vision The vision subsystem instance supplying camera data and pose estimations.
     */
    private fun updatePoseEstimationWithVision(vision: VisionSubsystem) {
        for (camera in vision.cameras) {
            val estimatedPose = vision.getEstimatedRobotPose(camera) ?: continue

            io.addVisionMeasurement(
                estimatedPose.estimatedPose.toPose2d(),
                estimatedPose.timestampSeconds,
                camera.currentStandardDeviations,
            )
        }
    }

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean) = io.setMotorBrake(brake)

    /**
 * Resets the robot's odometry to the specified pose.
 *
 * If no pose is provided, the odometry is reset to the default origin (Pose2d.kZero).
 *
 * @param pose the pose to reset the odometry to; defaults to Pose2d.kZero.
 */
    fun resetPose(pose: Pose2d = Pose2d.kZero) = io.resetPose(pose)

    /**
     * Commands the drive system to move using the specified translation and rotation values.
     *
     * This function delegates the movement command to the drive I/O interface, allowing for
     * field-oriented control and the option to use open-loop control.
     *
     * @param translation The desired translational movement as a 2D vector.
     * @param rotation The rotational movement command.
     * @param fieldOriented If true, movement is executed relative to the field's orientation.
     * @param openLoop If true, the system uses open-loop control.
     */
    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    ) = io.drive(translation, rotation, fieldOriented, openLoop)

    /**
 * Commands the drive system to move using the provided chassis speeds.
 *
 * Delegates the drive command to the underlying hardware interface represented by `io`.
 *
 * @param speeds The desired chassis speeds, encapsulating forward, lateral, and angular velocities.
 */
fun drive(speeds: ChassisSpeeds) = io.drive(speeds)
}
