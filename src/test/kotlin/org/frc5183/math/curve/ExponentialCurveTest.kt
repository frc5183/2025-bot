package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow

class ExponentialCurveTest {
    @Test
    fun `test exponential curve with positive base`() {
        val base = 3.0
        val curve = ExponentialCurve(base)

        assertEquals(0.0, curve(0.0), 0.0001)

        assertEquals((((1 + base).pow(0.5) - 1) / base), curve(0.5), 0.0001)
        assertEquals((((1 + base).pow(1.0) - 1) / base), curve(1.0), 0.0001)

        assertEquals(-1 * (((1 + base).pow(0.5) - 1) / base), curve(-0.5), 0.0001)
        assertEquals(-1 * (((1 + base).pow(1.0) - 1) / base), curve(-1.0), 0.0001)
    }

    @Test
    fun `test exponential curve with different bases`() {
        val curve1 = ExponentialCurve(1.0)
        assertEquals(0.0, curve1(0.0), 0.0001)
        assertEquals(1.0, curve1(1.0), 0.0001)
        assertEquals(-1.0, curve1(-1.0), 0.0001)

        val curve2 = ExponentialCurve(10.0)
        assertEquals(0.0, curve2(0.0), 0.0001)
        assertEquals((((1 + 10.0).pow(1.0) - 1) / 10.0), curve2(1.0), 0.0001)
        assertEquals(-1 * (((1 + 10.0).pow(1.0) - 1) / 10.0), curve2(-1.0), 0.0001)
    }
}
