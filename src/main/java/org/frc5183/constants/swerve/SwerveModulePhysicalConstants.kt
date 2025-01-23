package org.frc5183.constants.swerve

import edu.wpi.first.units.Units
import org.frc5183.constants.PhysicalConstants
import swervelib.parser.SwerveModulePhysicalCharacteristics
import swervelib.parser.json.modules.AngleConversionFactorsJson
import swervelib.parser.json.modules.ConversionFactorsJson
import swervelib.parser.json.modules.DriveConversionFactorsJson

object SwerveModulePhysicalConstants {
    /**
     * The [ConversionFactorsJson] for every swerve module.
     */
    val CONVERSION_FACTORS: ConversionFactorsJson =
        ConversionFactorsJson()

    /**
     * The [DriveConversionFactorsJson] for every swerve module.
     */
    val DRIVE_CONVERSION_FACTORS: DriveConversionFactorsJson =
        DriveConversionFactorsJson()

    /**
     * The [AngleConversionFactorsJson] for every swerve module.
     */
    val ANGLE_CONVERSION_FACTORS: AngleConversionFactorsJson =
        AngleConversionFactorsJson()

    /**
     * A representation of this [SwerveModulePhysicalConstants] as a [SwerveModulePhysicalCharacteristics].
     */
    val YAGSL: SwerveModulePhysicalCharacteristics
        get() =
            SwerveModulePhysicalCharacteristics(
                CONVERSION_FACTORS,
                PhysicalConstants.WHEEL_COF,
                PhysicalConstants.OPTIMAL_VOLTAGE.`in`(Units.Volts),
                PhysicalConstants.DRIVE_CURRENT_LIMIT.`in`(Units.Amps).toInt(),
                PhysicalConstants.ANGLE_CURRENT_LIMIT.`in`(Units.Amps).toInt(),
                PhysicalConstants.DRIVE_MOTOR_RAMP_RATE,
                PhysicalConstants.ANGLE_MOTOR_RAMP_RATE,
                PhysicalConstants.DRIVE_MINIMUM_VOLTAGE.`in`(Units.Volts),
                PhysicalConstants.ANGLE_MINIMUM_VOLTAGE.`in`(Units.Volts),
                PhysicalConstants.STEER_ROTATIONAL_INERTIA.`in`(Units.KilogramSquareMeters),
                PhysicalConstants.MASS.`in`(Units.Kilograms),
            )

    init {
        DRIVE_CONVERSION_FACTORS.gearRatio = PhysicalConstants.DRIVE_GEAR_RATIO
        DRIVE_CONVERSION_FACTORS.diameter =
            PhysicalConstants.WHEEL_RADIUS.`in`(Units.Inches) * 2
        DRIVE_CONVERSION_FACTORS.calculate()

        ANGLE_CONVERSION_FACTORS.gearRatio = PhysicalConstants.ANGLE_GEAR_RATIO
        // todo depending on the encoder we might have to do something different i think
        ANGLE_CONVERSION_FACTORS.calculate()

        CONVERSION_FACTORS.drive = DRIVE_CONVERSION_FACTORS
        CONVERSION_FACTORS.angle = ANGLE_CONVERSION_FACTORS
    }
}
