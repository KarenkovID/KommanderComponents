package com.kommander.components.domain_core.extensions

import org.junit.Assert
import org.junit.Test

class NumbersExtensionsKtTest {

    @Test
    fun test() {
        Assert.assertEquals("0", 0.toRankingString())
        Assert.assertEquals("1", 1.toRankingString())
        Assert.assertEquals("10", 10.toRankingString())
        Assert.assertEquals("100", 100.toRankingString())
        Assert.assertEquals("1 000", 1000.toRankingString())
        Assert.assertEquals("10 000", 10000.toRankingString())
        Assert.assertEquals("100 000", 100000.toRankingString())
        Assert.assertEquals("1 000 000", 1000000.toRankingString())
        Assert.assertEquals("-1 000", (-1000).toRankingString())
        Assert.assertEquals("-10 000", (-10000).toRankingString())
        Assert.assertEquals("-100 000", (-100000).toRankingString())
        Assert.assertEquals("-1 100 000", (-1100000).toRankingString())
    }
}