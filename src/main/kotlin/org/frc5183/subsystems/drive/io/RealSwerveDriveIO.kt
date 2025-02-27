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

    override fun updateInputs(inputs: SwerveDriveIO.SwerveDriveIOInputs) {
        inputs.pose = pose
        inputs.velocity = robotVelocity
        inputs.moduleStates = drive.states
    }

    override val pose: Pose2d
        get() = drive.pose

    override val robotVelocity
        get() = drive.robotVelocity

    override fun stopOdometryThread() = drive.stopOdometryThread()

    override fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    ) = drive.addVisionMeasurement(pose, timestampSeconds, standardDeviations)

    override fun updateOdometry() = drive.updateOdometry()

    override fun setMotorBrake(brake: Boolean) = drive.setMotorIdleMode(brake)

    override fun resetPose(pose: Pose2d) = drive.resetOdometry(pose)

    override fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    ) = drive.drive(translation, rotation, fieldOriented, openLoop)

    override fun drive(speeds: ChassisSpeeds) = drive.drive(speeds)
}
