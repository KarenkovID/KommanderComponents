package com.kommander.components.android_core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, onChanged: (T) -> Unit) = observe(lifecycleOwner, Observer(onChanged))