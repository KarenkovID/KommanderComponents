package com.kommander.components.android_core.viewmodel.livedata

sealed class Event {

    object Loading : Event()

    object Complete : Event()

    data class Error(val throwable: Throwable) : Event()

}
