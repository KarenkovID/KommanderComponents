package com.kommander.components.domain.dataProvider

import io.reactivex.Maybe
import java.io.File
import java.io.InputStream

interface ResourceManager {

    fun getString(resId: Int): String

    fun dp(value: Int): Int

    fun getPackageName(): String

    fun getAsset(name: String): Maybe<String>

    fun getAssetAsStream(name: String): InputStream

    fun getString(resId: Int, vararg args: Any): String

    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String

    fun getInternalFileStoragePath(): File

    fun getStringArray(resId: Int): Array<String>

}
