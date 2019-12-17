package com.kommander.components.domain_core.extensions

import java.util.Optional

val <T : Any> Optional<T>.isEmpty: Boolean
    get() = !isPresent

fun <T : Any> Optional<T>.toNullable(): T? = if (isPresent) get() else null

fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)