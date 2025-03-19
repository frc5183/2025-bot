package org.frc5183.constants

import com.revrobotics.spark.SparkLowLevel
import com.studica.frc.AHRS.NavXComType
import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Rotation3d
import edu.wpi.first.math.geometry.Transform3d
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.I2C
import org.frc5183.hardware.vision.Camera

/**
 * Device IDs connected to the robot.
 */
object DeviceConstants {
    const val ELEVATOR_MOTOR_ID: Int = 5
    val ELEVATOR_MOTOR_TYPE: SparkLowLevel.MotorType = SparkLowLevel.MotorType.kBrushless
    const val ELEVATOR_BOTTOM_LIMIT_SWITCH_ID: Int = 0
    const val ELEVATOR_TOP_LIMIT_SWITCH_ID: Int = 1

    const val CLIMBER_CAN: Int = 7
    val CLIMBER_MOTOR_TYPE: SparkLowLevel.MotorType = SparkLowLevel.MotorType.kBrushless
    const val CLIMBER_LIMIT_SWITCH_ID: Int = 2

    const val CORAL_MOTOR_ID: Int = 6
    val CORAL_MOTOR_TYPE: SparkLowLevel.MotorType = SparkLowLevel.MotorType.kBrushless // Neo 550
    val CORAL_COLOR_SENSOR_PORT: I2C.Port = I2C.Port.kOnboard

    val FRONT_CAMERA: Camera =
        Camera(
            "Front",
            Transform3d(Units.Inches.of(6.5), Units.Inches.of(6.0), Units.Inches.of(27.5), Rotation3d(0.0, 0.0, 0.0)),
            VecBuilder.fill(4.0, 4.0, 8.0),
            VecBuilder.fill(0.5, 0.5, 1.0),
        )

    /*
        val BACK_CAMERA: Camera =
            Camera(
                "Back",
                Transform3d(0.0, 0.0, 1.0, Rotation3d(0.0, 0.0, 180.0)),
                VecBuilder.fill(4.0, 4.0, 8.0),
                VecBuilder.fill(0.5, 0.5, 1.0),
            )

     */
    val IMU_PORT: NavXComType = NavXComType.kMXP_SPI
}
