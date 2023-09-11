package com.example.kotlin.test

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FailTests {
    @Test
    fun failTest() {
        Assertions.assertThat(1).isEqualTo(2)
    }
}