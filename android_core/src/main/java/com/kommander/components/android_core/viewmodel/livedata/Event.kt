package com.kommander.components.android_core.viewmodel.livedata

sealed class Event {

    val loading: Boolean
        get() = this is Loading

    val complete: Boolean
        get() = this is Complete

    val error: Boolean
        get() = this is Error

    object Loading : Event()

    object Complete : Event()

    data class Error(val throwable: Throwable) : Event()

}
