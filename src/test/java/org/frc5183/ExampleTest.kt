package org.frc5183

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExampleTest {
    // To learn more about how to write unit tests, see the
    // JUnit 5 User Guide at https://junit.org/junit5/docs/current/user-guide/

    // In Kotlin, we can write test names with spaces by enclosing the names in backticks.
    // This lets us use names with clearer test purpose & intent.
    @Test
    fun `string#lowerCase should handle mixed case and return all lower case`() {
        assertEquals("robot", "Robot".lowercase())
    }

    @Test
    fun `2 plus 2 should equal 4`() {
        assertEquals(4, 2 + 2)
    }
}
