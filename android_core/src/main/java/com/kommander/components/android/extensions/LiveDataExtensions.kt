package com.kommander.components.android.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, crossinline onChanged: (T) -> Unit) =
        observe(lifecycleOwner, Observer { onChanged(it) })

@Suppress("detekt.FunctionNaming")
fun <T> MutableLiveData(value: T): MutableLiveData<T> = MutableLiveData<T>().apply { this.value = value }