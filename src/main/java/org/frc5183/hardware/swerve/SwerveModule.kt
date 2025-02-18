package org.frc5183.hardware.swerve

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.motorcontrol.MotorController

class SwerveModule(driveMotor: MotorController
    , angleMotor: MotorController,
    motorEncoder: Encoder
    , absoluteEncoder: Encoder
    , driveMotorReversed: Boolean
    , angleMotorReversed: Boolean
    , absoluteEncoderReversed: Boolean
    , absoluteEncoderOffset: Double
    , encoderResolution: Double,
    wheelEncoderResolution: Double
    , wheelRadius: Double
    , pidDriveP: Double
    , pidDriveI: Double
    , pidDriveD: Double,
    pidTurnP: Double,
    pidTurnI: Double,
    pidTurnD: Double,
    driveKs: Double,
    driveKv: Double,
    turnKs: Double,
    turnKv: Double,
    maxTurningVelocity: Double,
    maxTurningAcceleration: Double,
    offsetTranslation: Translation2d
) {
    val driveMotor = driveMotor.apply { inverted = driveMotorReversed }
    val angleMotor = angleMotor.apply { inverted = angleMotorReversed }
    val motorEncoder = motorEncoder
    val absoluteEncoder = absoluteEncoder
    val motorFeedforwardDrive = SimpleMotorFeedforward(driveKs, driveKv)
    val motorFeedforwardTurn = SimpleMotorFeedforward(turnKs, turnKv)
    val pidDriveController = PIDController(pidDriveP, pidDriveI, pidDriveD)
    val offsetTranslation = offsetTranslation
    val pidTurnController = ProfiledPIDController(
        pidTurnP,
        pidTurnI,
        pidTurnD,
        TrapezoidProfile.Constraints(
            maxTurningVelocity,
            maxTurningAcceleration
        )
    )
    init {
        absoluteEncoder.distancePerPulse=2 * Math.PI / encoderResolution
        motorEncoder.distancePerPulse = 2 * Math.PI * wheelRadius / wheelEncoderResolution
        pidTurnController.enableContinuousInput(-Math.PI, Math.PI)
    }
    fun getState(): SwerveModuleState {
        return SwerveModuleState(motorEncoder.rate, Rotation2d(absoluteEncoder.distance))
    }

    fun getPosition(): SwerveModulePosition {
        return SwerveModulePosition(
            motorEncoder.distance,
            Rotation2d(absoluteEncoder.distance)
        )
    }
    fun setDesiredState(state: SwerveModuleState) {
        val encoderRotation = Rotation2d(absoluteEncoder.distance)
        state.optimize(encoderRotation)
        state.cosineScale(encoderRotation)
        val angle = state.angle
        val drive = state.speedMetersPerSecond
        val turnOutput = pidTurnController.calculate(absoluteEncoder.distance, angle.radians)
        val driveOutput = pidDriveController.calculate(motorEncoder.rate, drive)
        val turnFeedforward = motorFeedforwardTurn.calculate(turnOutput)
        val driveFeedforward = motorFeedforwardDrive.calculate(driveOutput)
        angleMotor.setVoltage(turnFeedforward + turnOutput)
        driveMotor.setVoltage(driveFeedforward + driveOutput)
    }
    fun getOffsetTranslation(): Translation2d {
        return offsetTranslation
    }
}