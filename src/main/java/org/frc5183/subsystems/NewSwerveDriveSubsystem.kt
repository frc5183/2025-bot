package org.frc5183.subsystems

import com.studica.frc.AHRS
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.geometry.Twist2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveDriveOdometry
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.hardware.swerve.SwerveModule

class NewSwerveDriveSubsystem(
    val maxSpeed: Double, // meters per second
    val maxAngularSpeed: Double, // radians per second
    val frontLeftSwerveModule: SwerveModule,
    val frontRightSwerveModule: SwerveModule,
    val backLeftSwerveModule: SwerveModule,
    val backRightSwerveModule: SwerveModule,
    val startTranslation: Translation2d,
    val startRotation: Rotation2d,
    val gyro: AHRS,
) : Subsystem {
    val kinematics = SwerveDriveKinematics(
        frontLeftSwerveModule.offsetTranslation,
        frontRightSwerveModule.offsetTranslation,
        backLeftSwerveModule.offsetTranslation,
        backRightSwerveModule.offsetTranslation,
    )
    val odometry = SwerveDrivePoseEstimator(
        kinematics,
        gyro.rotation2d,
        arrayOf(
            frontLeftSwerveModule.getPosition(),
            frontRightSwerveModule.getPosition(),
            backLeftSwerveModule.getPosition(),
            backRightSwerveModule.getPosition(),
        ),
        Pose2d(startTranslation,startRotation)
    )
    init {
        gyro.reset();
    }
    fun driveChassisSpeeds(speeds: ChassisSpeeds) {
        val moduleStates = kinematics.toSwerveModuleStates(speeds)
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, maxSpeed)
        frontLeftSwerveModule.setDesiredState(moduleStates[0])
        frontRightSwerveModule.setDesiredState(moduleStates[1])
        backLeftSwerveModule.setDesiredState(moduleStates[2])
        backRightSwerveModule.setDesiredState(moduleStates[3])
    }
    fun drive(x: Double, y: Double, rotation: Double, fieldRelative: Boolean = false, periodSeconds: Double) {
        val speeds = ChassisSpeeds.discretize(
            if (fieldRelative) {
                ChassisSpeeds.fromFieldRelativeSpeeds(x*maxSpeed, y*maxSpeed, rotation*maxAngularSpeed, gyro.rotation2d)
            } else {
                ChassisSpeeds(x, y, rotation)
            },
            periodSeconds)
        driveChassisSpeeds(speeds)
    }
    fun feedPoseEstimator() { // Should be called periodically
        odometry.update(gyro.rotation2d, arrayOf(
            frontLeftSwerveModule.getPosition(),
            frontRightSwerveModule.getPosition(),
            backLeftSwerveModule.getPosition(),
            backRightSwerveModule.getPosition(),
        ))
    }
    fun addVisionMeasurement(pose: Pose2d, timestampSeconds: Double) { // Should be called periodically, if using vision
        odometry.addVisionMeasurement(pose, timestampSeconds)
    }
}