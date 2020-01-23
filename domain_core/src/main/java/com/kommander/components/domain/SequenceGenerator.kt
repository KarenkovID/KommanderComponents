package com.kommander.components.domain

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object SequenceGenerator {

    private val intSequence = AtomicInteger(0)
    private val longSequence = AtomicLong(0)

    fun nextLong(): Long = longSequence.incrementAndGet()

    fun nextInt(): Int = intSequence.incrementAndGet()

}