package com.kommander.components.android.network

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class TimberHttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        Timber.tag(TIMBER_TAG).i(message)
    }

    companion object {
        private const val TIMBER_TAG = "OkHttp"
    }

}