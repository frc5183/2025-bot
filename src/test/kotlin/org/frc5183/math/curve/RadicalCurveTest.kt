package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.sqrt

class RadicalCurveTest {
    @Test
    fun `test radical curve with positive a and b`() {
        val a = 1.0
        val b = 0.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(0.0), 0.0001)

        assertEquals(sqrt(1.0), curve(1.0), 0.0001) // 1.0/1.0 * (1.0 * sqrt(1.0) + 0.0) = 1.0
        assertEquals(2.0 / 2.0 * (1.0 * sqrt(2.0) + 0.0), curve(2.0), 0.0001)

        assertEquals(-sqrt(1.0), curve(-1.0), 0.0001) // -1.0/1.0 * (1.0 * sqrt(1.0) + 0.0) = -1.0
        assertEquals(-2.0 / 2.0 * (1.0 * sqrt(2.0) + 0.0), curve(-2.0), 0.0001)
    }

    @Test
    fun `test radical curve with positive a and negative b`() {
        val a = 1.0
        val b = -1.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(1.0), 0.0001) // 1.0 * sqrt(1.0) - 1.0 = 0.0
        assertEquals((1.0 * sqrt(abs(4.0))) + -1.0, curve(4.0), 0.0001) //  1.0 * sqrt(abs(4.0)) - 1.0 = 1.0

        assertEquals(0.0, curve(-1.0), 0.0001) // -1.0 * (1.0 * sqrt(1.0) - 1.0) = 0.0
        assertEquals(-1.0 * ((1.0 * sqrt(abs(-4.0))) + -1.0), curve(-4.0), 0.0001) // -1.0 * (1.0 * sqrt(abs(4.0)) - 1.0) = -1.0
    }

    @Test
    fun `test radical curve with negative a and positive b`() {
        val a = -1.0
        val b = 1.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(1.0), 0.0001) // -1.0 * sqrt(1.0) - 1.0 = 0.0
        assertEquals((-1.0 * sqrt(abs(4.0))) + 1.0, curve(4.0), 0.0001) //  -1.0 * sqrt(abs(4.0)) + 1.0 = -1.0

        assertEquals(0.0, curve(-1.0), 0.0001) // -1.0 * (-1.0 * sqrt(1.0) - 1.0) = 0.0
        assertEquals(-1.0 * ((-1.0 * sqrt(abs(-4.0))) + 1.0), curve(-4.0), 0.0001) // -1.0 * (-1.0 * sqrt(abs(4.0)) + 1.0) = 1.0
    }
}
