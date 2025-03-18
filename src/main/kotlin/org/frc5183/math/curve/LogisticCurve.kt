package org.frc5183.math.curve

import kotlin.math.E
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign

/**
 * Represents a logistic [Curve] with some modifications:
 * - Symmetric about the y-axis, allowing for negative inputs.
 * - Uses a base of 1 + e. (where e is Euler's number). Applies an asymptote to the curve at y= -1 or 1.
 * - Fills the holes at x = -1 and 1 with -1 and 1 respectively.
 *
 * The equation for this curve is y = 1 / (1 + e^(-[a] * abs(x) - [b]))
 */

class LogisticCurve(
    /**
     * The [a] constant in the equation.
     */
    val a: Double,
    /**
     * The [b] constant in the equation.
     */
    val b: Double,
) : Curve {
    override operator fun invoke(input: Double): Double {
        if (input == 1.0) return 1.0
        if (input == -1.0) return -1.0

        val sign = if (input == 0.0) 1.0 else input.sign

        return sign * (1 / (1 + E.pow(-a * abs(input) - b)))
    }
}
