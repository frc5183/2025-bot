package org.frc5183.math.curve

import kotlin.math.sqrt

/**
 * Represents a radical [Curve] in the form of y = [a]sqrt(x) + [b]. (where y is the output and x is the input)
 */
class RadicalCurve(
    /**
     * The coefficient of the sqrt(x) term in the radical equation.
     */
    val a: Double,

    /**
     * The constant term in the radical equation.
     */
    val b: Double
) : Curve {
    override fun invoke(input: Double): Double {
        return a * sqrt(input) + b
    }
}