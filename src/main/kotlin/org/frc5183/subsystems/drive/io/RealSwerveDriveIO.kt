package org.frc5183.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import org.frc5183.constants.swerve.SwerveConstants
import swervelib.SwerveDrive

// todo see SimulatedSwerveDriveIO
open class RealSwerveDriveIO : SwerveDriveIO {
    val drive: SwerveDrive = SwerveConstants.YAGSL_DRIVE

    init {
        drive.setHeadingCorrection(false); // Should only be enabled when controlling the robot by angle.
    }

    /**
     * Updates the swerve drive inputs with the current state.
     *
     * This method updates the provided inputs object by assigning it the latest pose, robot velocity,
     * and module states from the drive system.
     *
     * @param inputs the container for swerve drive input data
     */
    override fun updateInputs(inputs: SwerveDriveIO.SwerveDriveIOInputs) {
        inputs.pose = pose
        inputs.velocity = robotVelocity
        inputs.moduleStates = drive.states
    }

    override val pose: Pose2d
        get() = drive.pose

    override val robotVelocity
        get() = drive.robotVelocity

    /**
 * Stops the odometry thread that continuously updates the robot's position.
 *
 * This method signals the underlying drive system to halt odometry updates,
 * which is useful when odometry is no longer needed.
 */
override fun stopOdometryThread() = drive.stopOdometryThread()

    /**
     * Incorporates an external vision measurement into the drive's odometry.
     *
     * This method forwards a vision-based pose measurement, including its timestamp and associated uncertainties,
     * to the underlying swerve drive system for integration into the current odometry state.
     *
     * @param pose the robot's pose obtained from vision processing, expressed in field coordinates.
     * @param timestampSeconds the moment, in seconds, when the vision measurement was recorded.
     * @param standardDeviations a 3x1 matrix of standard deviations representing the uncertainties in the pose measurement.
     */
    override fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    ) = drive.addVisionMeasurement(pose, timestampSeconds, standardDeviations)

    /**
 * Updates the robot's odometry by delegating to the underlying drive system.
 */
override fun updateOdometry() = drive.updateOdometry()

    /**
 * Configures the drive's motor idle mode.
 *
 * When `brake` is true, the drive motors are set to actively brake; otherwise, they are set to coast.
 *
 * @param brake if true, enables brake mode; if false, disables brake mode allowing coasting.
 */
override fun setMotorBrake(brake: Boolean) = drive.setMotorIdleMode(brake)

    /**
 * Resets the robot's odometry to the specified pose.
 *
 * This method delegates the odometry reset to the underlying drive system.
 *
 * @param pose the new pose for the robot's odometry.
 */
override fun resetPose(pose: Pose2d) = drive.resetOdometry(pose)

    /**
     * Commands the swerve drive with specified translation and rotation inputs.
     *
     * This method instructs the drive system to move based on the given translation vector and
     * rotational value. It supports field-oriented control and open-loop operation, enabling flexible
     * drive configurations.
     *
     * @param translation The desired translation vector.
     * @param rotation The rotational command.
     * @param fieldOriented If true, interprets the command using field-relative coordinates.
     * @param openLoop If true, executes the command in open-loop mode.
     */
    override fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    ) = drive.drive(translation, rotation, fieldOriented, openLoop)

    /**
 * Commands the robot to move using the specified chassis speeds.
 *
 * @param speeds The desired chassis speeds for controlling the robot's movement.
 */
override fun drive(speeds: ChassisSpeeds) = drive.drive(speeds)
}
