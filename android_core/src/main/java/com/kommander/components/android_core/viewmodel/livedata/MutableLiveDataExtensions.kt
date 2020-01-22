package com.kommander.components.android_core.viewmodel.livedata

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Event>.loading() {
    value = Event.Loading
}

fun MutableLiveData<Event>.error(throwable: Throwable) {
    value = Event.Error(throwable)
}

fun MutableLiveData<Event>.complete() {
    value = Event.Complete
}