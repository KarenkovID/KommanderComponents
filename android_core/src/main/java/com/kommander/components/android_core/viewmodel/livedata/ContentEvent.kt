package com.kommander.components.android_core.viewmodel.livedata

import java.lang.Error

sealed class ContentEvent<out T>(open val data: T?) {

    fun isLoading() = this is Loading

    inline fun isError() = this is Error

    data class Loading<out T>(override val data: T? = null) : ContentEvent<T>(data)

    data class Success<out T>(override val data: T) : ContentEvent<T>(data)

    data class Error<out T>(val throwable: Throwable, override val data: T? = null) : ContentEvent<T>(data)

    data class Complete<out T>(override val data: T? = null) : ContentEvent<T>(data)

}
