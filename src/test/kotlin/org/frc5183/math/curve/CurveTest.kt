package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CurveTest {
    @Test
    fun `test curve interface implementation`() {
        val curve = Curve { input: Double -> input * 2.0 }

        assertEquals(0.0, curve(0.0))
        assertEquals(2.0, curve(1.0))
        assertEquals(-2.0, curve(-1.0))
        assertEquals(10.0, curve(5.0))
    }
}
