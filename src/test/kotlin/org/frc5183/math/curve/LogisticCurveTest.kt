package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.E
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign

class LogisticCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }

    /**
     * Helper function to manually calculate the expected output of LogisticCurve,
     * allowing us to verify the implementation independently.
     */
    private fun calculateLogisticCurveValue(
        input: Double,
        a: Double,
        b: Double,
    ): Double =
        when (input) {
            0.0 -> 0.0
            -1.0 -> -1.0
            1.0 -> 1.0
            else -> input.sign * (1 / (1 + E.pow(-a * (abs(input) - b))))
        }

    @Test
    fun `test logistic curve with standard parameters`() {
        val a = 10.0
        val b = 0.0
        val curve = LogisticCurve(a, b)

        // Test within the range [-1, 1]
        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(1.0, curve(1.0), DELTA)
        assertEquals(-1.0, curve(-1.0), DELTA)

        // Test some points in between
        assertEquals(calculateLogisticCurveValue(0.5, a, b), curve(0.5), DELTA)
        assertEquals(calculateLogisticCurveValue(-0.5, a, b), curve(-0.5), DELTA)
        assertEquals(calculateLogisticCurveValue(0.25, a, b), curve(0.25), DELTA)
        assertEquals(calculateLogisticCurveValue(-0.75, a, b), curve(-0.75), DELTA)
    }

    @Test
    fun `test logistic curve with different a values`() {
        // Test with a = 0
        val curve1 = LogisticCurve(0.0, 0.0)
        assertEquals(0.0, curve1(0.0), DELTA)
        assertEquals(1.0, curve1(1.0), DELTA)
        assertEquals(-1.0, curve1(-1.0), DELTA)

        // Test with a small positive a
        val curve2 = LogisticCurve(1.0, 0.0)
        assertEquals(0.0, curve2(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, 1.0, 0.0), curve2(0.5), DELTA)

        // Test with a large positive a (steeper curve)
        val curve3 = LogisticCurve(20.0, 0.0)
        assertEquals(0.0, curve3(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, 20.0, 0.0), curve3(0.5), DELTA)

        // Test with a negative a
        val curve4 = LogisticCurve(-5.0, 0.0)
        assertEquals(0.0, curve4(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, -5.0, 0.0), curve4(0.5), DELTA)
    }

    @Test
    fun `test logistic curve with different b values within -1 and 1`() {
        val a = 5.0

        // Test with b = -1
        val curve1 = LogisticCurve(a, -1.0)
        assertEquals(calculateLogisticCurveValue(0.0, a, -1.0), curve1(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, a, -1.0), curve1(0.5), DELTA)

        // Test with b = 0
        val curve2 = LogisticCurve(a, 0.0)
        assertEquals(calculateLogisticCurveValue(0.0, a, 0.0), curve2(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, a, 0.0), curve2(0.5), DELTA)

        // Test with b = 1
        val curve3 = LogisticCurve(a, 1.0)
        assertEquals(calculateLogisticCurveValue(0.0, a, 1.0), curve3(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, a, 1.0), curve3(0.5), DELTA)
    }

    @Test
    fun `test logistic curve with values outside normal ranges`() {
        val a = 5.0
        val b = 0.0
        val curve = LogisticCurve(a, b)

        // Test with large positive inputs
        assertEquals(calculateLogisticCurveValue(2.0, a, b), curve(2.0), DELTA)
        assertEquals(calculateLogisticCurveValue(5.0, a, b), curve(5.0), DELTA)
        assertEquals(calculateLogisticCurveValue(10.0, a, b), curve(10.0), DELTA)

        // Test with large negative inputs
        assertEquals(calculateLogisticCurveValue(-2.0, a, b), curve(-2.0), DELTA)
        assertEquals(calculateLogisticCurveValue(-5.0, a, b), curve(-5.0), DELTA)
        assertEquals(calculateLogisticCurveValue(-10.0, a, b), curve(-10.0), DELTA)
    }

    @Test
    fun `test logistic curve with b values outside -1 and 1`() {
        val a = 5.0

        // Test with large positive b
        val curve1 = LogisticCurve(a, 5.0)
        assertEquals(calculateLogisticCurveValue(0.0, a, 5.0), curve1(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, a, 5.0), curve1(0.5), DELTA)

        // Test with large negative b
        val curve2 = LogisticCurve(a, -5.0)
        assertEquals(calculateLogisticCurveValue(0.0, a, -5.0), curve2(0.0), DELTA)
        assertEquals(calculateLogisticCurveValue(0.5, a, -5.0), curve2(0.5), DELTA)
    }

    @Test
    fun `test logistic curve symmetry`() {
        val curve = LogisticCurve(5.0, 0.0)

        // For all x values, curve(-x) should equal -curve(x)
        val testValues = listOf(0.1, 0.25, 0.5, 0.75, 0.9, 1.5, 2.0, 5.0)

        for (x in testValues) {
            assertEquals(
                -curve(x),
                curve(-x),
                DELTA,
                "Symmetry failed for x=$x: curve($x)=${curve(x)}, curve(-$x)=${curve(-x)}",
            )
        }
    }

    @Test
    fun `test logistic curve with edge case inputs`() {
        val curve = LogisticCurve(5.0, 0.0)

        // Test with NaN
        assertTrue(curve(Double.NaN).isNaN())

        // Test with infinities
        val posInfResult = curve(Double.POSITIVE_INFINITY)
        assertEquals(1.0, posInfResult, DELTA)

        val negInfResult = curve(Double.NEGATIVE_INFINITY)
        assertEquals(-1.0, negInfResult, DELTA)

        // Test with very small values close to zero
        val verySmallValue = 1e-10
        assertEquals(calculateLogisticCurveValue(verySmallValue, 5.0, 0.0), curve(verySmallValue), DELTA)
    }

    @Test
    fun `test logistic curve with extreme a and b values`() {
        // Test with very large a
        val curve1 = LogisticCurve(1000.0, 0.0)
        assertEquals(0.0, curve1(0.0), DELTA)
        assertEquals(1.0, curve1(0.01), DELTA) // Should approach 1 very quickly
        assertEquals(-1.0, curve1(-0.01), DELTA) // Should approach -1 very quickly

        // Test with very large negative a
        val curve2 = LogisticCurve(-1000.0, 0.0)
        assertEquals(0.0, curve2(0.0), DELTA)
        assertEquals(0.0, curve2(0.01), DELTA) // Should approach 0 very quickly
        assertEquals(0.0, curve2(-0.01), DELTA) // Should approach 0 very quickly

        // Test with very large b
        val curve3 = LogisticCurve(5.0, 100.0)
        assertEquals(calculateLogisticCurveValue(0.0, 5.0, 100.0), curve3(0.0), DELTA)

        // Test with very large negative b
        val curve4 = LogisticCurve(5.0, -100.0)
        assertEquals(calculateLogisticCurveValue(0.0, 5.0, -100.0), curve4(0.0), DELTA)
        assertTrue(curve4(0.0) < 0.01, "With large negative b, output at x=0 should be close to 0")
    }
}
