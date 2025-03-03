package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MultiCurveTest {
    @Test
    fun `test multi curve with linear curves`() {
        val curve1 = LinearCurve(2.0, 0.0) // y = 2x
        val curve2 = LinearCurve(1.0, 1.0) // y = x + 1
        val multiCurve = MultiCurve(listOf(curve1, curve2))

        // Test various inputs
        // Input 0.0 -> curve1(0.0) = 0.0 -> curve2(0.0) = 1.0
        assertEquals(1.0, multiCurve(0.0), 0.0001)

        // Input 1.0 -> curve1(1.0) = 2.0 -> curve2(2.0) = 3.0
        assertEquals(3.0, multiCurve(1.0), 0.0001)

        // Input -1.0 -> curve1(-1.0) = -2.0 -> curve2(-2.0) = -1.0
        assertEquals(-1.0, multiCurve(-1.0), 0.0001)
    }

    @Test
    fun `test multi curve with mixed curves`() {
        val curve1 = LinearCurve(2.0, 0.0) // y = 2x
        val curve2 = LimitedCurve(0.0, 2.0) // limit output to [0, 2]
        val multiCurve = MultiCurve(listOf(curve1, curve2))

        // Test various inputs
        // Input 0.0 -> curve1(0.0) = 0.0 -> curve2(0.0) = 0.0
        assertEquals(0.0, multiCurve(0.0), 0.0001)

        // Input 1.0 -> curve1(1.0) = 2.0 -> curve2(2.0) = 2.0
        assertEquals(2.0, multiCurve(1.0), 0.0001)

        // Input -1.0 -> curve1(-1.0) = -2.0 -> curve2(-2.0) = 0.0 (limited)
        assertEquals(0.0, multiCurve(-1.0), 0.0001)

        // Input 2.0 -> curve1(2.0) = 4.0 -> curve2(4.0) = 2.0 (limited)
        assertEquals(2.0, multiCurve(2.0), 0.0001)
    }

    @Test
    fun `test multi curve with empty list`() {
        val multiCurve = MultiCurve(listOf())

        // With no curves, the input should be returned as is
        assertEquals(0.0, multiCurve(0.0), 0.0001)
        assertEquals(1.0, multiCurve(1.0), 0.0001)
        assertEquals(-1.0, multiCurve(-1.0), 0.0001)
    }
}
