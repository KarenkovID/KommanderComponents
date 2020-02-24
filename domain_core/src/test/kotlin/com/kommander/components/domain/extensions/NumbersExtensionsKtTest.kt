package com.kommander.components.domain.extensions;

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NumbersExtensionsKtTest {

    @Test
    fun test() {
        assertEquals("0", 0.toRankingString())
        assertEquals("1", 1.toRankingString())
        assertEquals("10", 10.toRankingString())
        assertEquals("100", 100.toRankingString())
        assertEquals("1 000", 1000.toRankingString())
        assertEquals("10 000", 10000.toRankingString())
        assertEquals("100 000", 100000.toRankingString())
        assertEquals("1 000 000", 1000000.toRankingString())
        assertEquals("-1 000", (-1000).toRankingString())
        assertEquals("-10 000", (-10000).toRankingString())
        assertEquals("-100 000", (-100000).toRankingString())
        assertEquals("-1 100 000", (-1100000).toRankingString())
    }

}