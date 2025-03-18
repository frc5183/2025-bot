package org.frc5183.constants

import edu.wpi.first.wpilibj.RobotBase

object State {
    enum class Mode {
        REAL,
        SIM,
        REPLAY,
    }

    val mode: Mode =
        when {
            // https://github.com/Mechanical-Advantage/AdvantageKit/issues/173
            System.getenv()["AKIT_LOG_PATH"] != null -> Mode.REPLAY
            RobotBase.isReal() -> Mode.REAL
            else -> Mode.SIM
        }

    // val mode: Mode = Mode.REPLAY
}
