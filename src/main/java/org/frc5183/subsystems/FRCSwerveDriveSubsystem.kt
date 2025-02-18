package org.frc5183.subsystems

import com.ctre.phoenix6.controls.VelocityDutyCycle
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveDriveOdometry
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.motorcontrol.Talon
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.swerve.BackLeftSwerve
import org.frc5183.constants.swerve.BackRightSwerve
import org.frc5183.constants.swerve.FrontLeftSwerve
import org.frc5183.constants.swerve.FrontRightSwerve

object FRCSwerveDriveSubsystem : Subsystem {
    val frontLeftDrive = TalonFX(FrontLeftSwerve.driveMotorId)
    val frontRightDrive = TalonFX(FrontRightSwerve.driveMotorId)
    val backLeftDrive = TalonFX(BackLeftSwerve.driveMotorId)
    val backRightDrive = TalonFX(BackRightSwerve.driveMotorId)

    val frontLeftAngle = TalonFX(FrontLeftSwerve.angleMotorId)
    val frontRightAngle = TalonFX(FrontRightSwerve.angleMotorId)
    val backLeftAngle = TalonFX(BackLeftSwerve.angleMotorId)
    val backRightAngle = TalonFX(BackRightSwerve.angleMotorId)

    val frontLeftEncoder = CANcoder(FrontLeftSwerve.absoluteEncoderId)
    val frontRightEncoder = CANcoder(FrontRightSwerve.absoluteEncoderId)
    val backLeftEncoder = CANcoder(BackLeftSwerve.absoluteEncoderId)
    val backRightEncoder = CANcoder(BackRightSwerve.absoluteEncoderId)

    val kinematics = SwerveDriveKinematics(
        FrontLeftSwerve.location,
        FrontRightSwerve.location,
        BackLeftSwerve.location,
        BackRightSwerve.location
    )

    override fun periodic() {

    }

    fun drive(moduleStates: List<SwerveModuleState>) {
        drive(moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3])
    }

    private fun drive(
        frontLeftModule: SwerveModuleState,
        frontRightModule: SwerveModuleState,
        backLeftModule: SwerveModuleState,
        backRightModule: SwerveModuleState
    ) {
        frontLeftModule.optimize(Rotation2d(frontLeftEncoder.absolutePosition.refresh().value.`in`(Units.Degrees)))
        frontRightModule.optimize(Rotation2d(frontRightEncoder.absolutePosition.refresh().value.`in`(Units.Degrees)))
        backLeftModule.optimize(Rotation2d(backLeftEncoder.absolutePosition.refresh().value.`in`(Units.Degrees)))
        backRightModule.optimize(Rotation2d(backRightEncoder.absolutePosition.refresh().value.`in`(Units.Degrees)))


    }
}