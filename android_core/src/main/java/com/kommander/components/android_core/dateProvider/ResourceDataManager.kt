package com.kommander.components.android_core.dateProvider

import android.content.Context
import androidx.annotation.PluralsRes
import com.kommander.components.domain_core.dataProvider.ResourceManager
import io.reactivex.Maybe
import toothpick.InjectConstructor
import java.io.File

@InjectConstructor
class ResourceDataManager(private val context: Context) : ResourceManager {

    override fun getString(resId: Int): String = context.getString(resId)

    @Suppress("detekt.SpreadOperator")
    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)

    override fun dp(value: Int): Int {
        if (value == 0) {
            return 0
        }
        return context.resources?.displayMetrics?.let { metrics ->
            (value * metrics.density).toInt()
        } ?: 0
    }

    override fun getPackageName(): String = context.applicationContext.packageName

    @Suppress("detekt.SpreadOperator")
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
        return if (formatArgs.isEmpty()) {
            context.resources.getQuantityString(id, quantity)
        } else {
            context.resources.getQuantityString(id, quantity, *formatArgs)
        }
    }

    override fun getAsset(name: String): Maybe<String> {
        return Maybe.fromCallable {
            getAssetAsStream(name).use { it.reader().readText() }
        }
    }

    override fun getAssetAsStream(name: String) = context.assets.open(name)

    override fun getInternalFileStoragePath(): File = context.filesDir

    override fun getStringArray(resId: Int): Array<String> = context.resources.getStringArray(resId)
}