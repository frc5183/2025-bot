package org.frc5183.commands.drive

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.PhysicalConstants
import org.frc5183.math.curve.Curve
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A command that allows the driver to control the robot's translation and rotation using the joystick.
 */
class TeleopDriveCommand(
    /**
     * The [SwerveDriveSubsystem] to control.
     */
    val drive: SwerveDriveSubsystem,
    /**
     * A function that returns the X translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val xInput: () -> Double,
    /**
     * A function that returns the Y translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val yInput: () -> Double,
    /**
     * A function that returns the rotation input for the robot. This should be in the range [-1, 1] and will
     * have [rotationCurve] applied, then scaled to [PhysicalConstants.MAX_ANGULAR_VELOCITY].
     */
    val rotationInput: () -> Double,
    /**
     * The [Curve] to apply to the translation inputs.
     */
    val translationCurve: Curve,
    /**
     * The [Curve] to apply to the rotation input.
     */
    val rotationCurve: Curve,
    /**
     * Whether to use field-relative control.
     */
    val fieldRelative: Boolean,
) : Command() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {}

    override fun execute() {
        var translation = applyCurveToTranslation(Translation2d(xInput(), yInput()), translationCurve)

        if (fieldRelative) {
            translation = applyAllianceAwareTranslation(translation)
        }

        val xSpeed = PhysicalConstants.MAX_SPEED * translation.x
        val ySpeed = PhysicalConstants.MAX_SPEED * translation.y

        drive.drive(
            if (fieldRelative) {
                ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed,
                    ySpeed,
                    PhysicalConstants.MAX_ANGULAR_VELOCITY * rotationCurve(rotationInput()),
                    drive.pose.rotation,
                )
            } else {
                ChassisSpeeds.fromRobotRelativeSpeeds(
                    xSpeed,
                    ySpeed,
                    PhysicalConstants.MAX_ANGULAR_VELOCITY * rotationCurve(rotationInput()),
                    drive.pose.rotation,
                )
            },
        )
    }

    fun applyCurveToTranslation(
        translation: Translation2d,
        curve: Curve,
    ): Translation2d {
        val angle = atan2(translation.x, translation.y)
        var magnitude = curve(sqrt(translation.x.pow(2) + translation.y.pow(2).coerceIn(0.0, 1.0)))
        return Translation2d(magnitude * cos(angle), magnitude * sin(angle))
    }

    fun applyAllianceAwareTranslation(fieldRelativeTranslation: Translation2d): Translation2d =
        if (fieldRelative && DriverStation.getAlliance().orElseGet { DriverStation.Alliance.Red } == DriverStation.Alliance.Blue) {
            fieldRelativeTranslation.rotateBy(Rotation2d.k180deg)
        } else {
            fieldRelativeTranslation
        }

    override fun isFinished(): Boolean = false

    override fun end(interrupted: Boolean) {}
}
