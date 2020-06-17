package com.kommander.components.android.dateProvider

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified TValue> preferencesDelegate(preferences: SharedPreferences, name: String, defValue: TValue? = null) = GenericArg<TValue>(
        {
            when (TValue::class) {
                Boolean::class -> preferences.getBoolean(name, (defValue as Boolean?) ?: false)
                String::class -> preferences.getString(name, defValue as String?)
                Int::class -> preferences.getInt(name, (defValue as Int?) ?: 0)
                else -> throw TypeNotPresentException(TValue::class.java.simpleName, null)
            } as TValue
        },
        { value: TValue ->
            when (TValue::class) {
                Boolean::class -> preferences.edit().putBoolean(name, value as Boolean).apply()
                String::class -> preferences.edit().putString(name, value as String).apply()
                Int::class -> preferences.edit().putInt(name, value as Int).apply()
                else -> throw TypeNotPresentException(TValue::class.java.simpleName, null)
            }
        }
)

class GenericArg<TValue : Any?>(
        private val get: () -> TValue,
        private val set: (TValue) -> Unit
) : ReadWriteProperty<Any, TValue> {

    override fun getValue(thisRef: Any, property: KProperty<*>): TValue = get()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: TValue) {
        set(value)
    }
}