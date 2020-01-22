package com.kommander.components.domain_core.extensions

import java.lang.StringBuilder

fun Int.toRankingString(rank: Int = 3): String{
    val str = toString()
    val res = StringBuilder(str)
    var startPos = str.length % rank
    if (startPos == 0) {
        startPos = rank
    }
    if (this < 0 && startPos == 1) {
        startPos += rank
    }
    while (startPos < res.length) {
        res.insert(startPos, ' ')
        startPos += rank + 1
    }
    return res.toString()
}