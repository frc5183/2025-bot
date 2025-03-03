package org.frc5183.math.curve

import edu.wpi.first.wpilibj.Timer
import kotlin.time.Duration

/**
 * Represents a curve that returns zero after a certain [time] has passed. Otherwise,
 * it returns the input without modification.
 *
 * Begins counting time from the moment the curve is constructed.
 */
class TimedCurve(
    /**
     * The duration of time after which the curve will return zero.
     */
    val time: Duration
) : Curve {
    /**
     * The [Timer] which tracks the time since the curve was created.
     */
    private val timer = Timer()

    init {
        timer.start()
    }

    override fun invoke(input: Double): Double {
        return if (timer.hasElapsed(time.inWholeSeconds.toDouble())) {
            input
        } else {
            0.0
        }
    }
}