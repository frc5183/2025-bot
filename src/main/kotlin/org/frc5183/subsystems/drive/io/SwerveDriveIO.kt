package org.frc5183.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import org.frc5183.log.AutoLogInputs

interface SwerveDriveIO {
    class SwerveDriveIOInputs : AutoLogInputs() {
        var pose by log(Pose2d())
        var velocity by log(ChassisSpeeds())
        var moduleStates by log(arrayOf<SwerveModuleState>())
    }

    /**
 * Updates the swerve drive's internal state using the latest sensor inputs.
 *
 * @param inputs the input data containing the robot's current pose, velocity, and swerve module states.
 */
fun updateInputs(inputs: SwerveDriveIOInputs)

    /**
     * The [Pose2d] of the robot according to swerve drive odometry and vision (if enabled).
     */
    val pose: Pose2d

    /**
     * The [ChassisSpeeds] of the robot according to swerve drive odometry.
     */
    val robotVelocity: ChassisSpeeds

    /**
     * Stops the swerve drive's odometry thread.
     * If you do this you need to manually call [updateOdometry]
     */
    fun stopOdometryThread()

    /**
     * Incorporates a vision-based pose measurement into the swerve drive system.
     *
     * This method registers an external vision measurement, including the observed pose,
     * the timestamp when the measurement was captured (in seconds), and the associated uncertainty
     * expressed through a matrix of standard deviations. These vision measurements are typically
     * used to enhance the robot's state estimation by fusing them with other sensor data.
     *
     * @param pose the vision-observed pose to be integrated into the system.
     * @param timestampSeconds the time in seconds when the vision measurement was recorded.
     * @param standardDeviations the measurement uncertainties associated with the vision observation.
     */
    fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    )

    /**
 * Updates the robot's odometry using YAGSL.
 *
 * This method recalculates the robot's pose from sensor data and should only be called
 * after stopping the odometry thread, which is typically necessary when vision-based pose
 * estimation is active.
 */
    fun updateOdometry()

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean)

    /**
 * Resets the robot's internal pose to the specified value.
 *
 * This method updates the robot's odometry to the provided [pose]. If no pose is specified, it defaults to [Pose2d.kZero].
 *
 * @param pose The new pose to set for the robot, defaulting to [Pose2d.kZero].
 */
    fun resetPose(pose: Pose2d = Pose2d.kZero)

    /**
     * Drives the robot by applying the specified translation and rotation commands.
     *
     * This function supports both field-oriented and open-loop control modes.
     * When field-oriented mode is enabled, the translation vector is interpreted
     * relative to the field rather than the robot's current orientation.
     *
     * @param translation The 2D vector representing the desired translational movement.
     * @param rotation The rotational command, typically representing angular velocity.
     * @param fieldOriented If true, interprets the translation in a field-relative frame.
     * @param openLoop If true, engages open-loop control, bypassing sensor feedback.
     */
    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    )

    /**
     * Drives the robot with the given chassis speeds.
     * @param speeds The chassis speeds to drive with.
     */
    fun drive(speeds: ChassisSpeeds)
}
