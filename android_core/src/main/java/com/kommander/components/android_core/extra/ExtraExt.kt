package com.kommander.components.android_core.extra

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable
import java.util.Date
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

@Suppress("UNCHECKED_CAST")
inline fun <reified T> args(): ReadWriteProperty<Fragment, T> = GenericFragmentArg(
        { name ->
            @Suppress("IMPLICIT_CAST_TO_ANY")
            when (T::class) {
                Boolean::class -> getBoolean(name)
                String::class -> getString(name)
                Int::class -> getInt(name)
                Date::class -> Date().apply { time = getLong(name) }
                else -> throw TypeNotPresentException(T::class.java.simpleName, null)
            }
        },
        { name, value ->
            when (T::class) {
                Boolean::class -> putBoolean(name, value as Boolean)
                String::class -> putString(name, value as String)
                Int::class -> putInt(name, value as Int)
                Date::class -> putLong(name, (value as Date).time)
                else -> throw TypeNotPresentException(T::class.java.simpleName, null)
            }
        }
) as ReadWriteProperty<Fragment, T>

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Serializable> serializedArgs(): ReadWriteProperty<Fragment, T> = GenericFragmentArg(
        get = { name -> getSerializable(name) },
        set = { name, value -> putSerializable(name, value as Serializable) }
) as ReadWriteProperty<Fragment, T>

@Suppress("UNCHECKED_CAST")
inline fun <reified T> activityArgs(): ReadOnlyProperty<FragmentActivity, T> = GenericActivityArg { name ->
    @Suppress("IMPLICIT_CAST_TO_ANY")
    when (T::class) {
        Boolean::class -> getBoolean(name)
        String::class -> getString(name)
        Int::class -> getInt(name)
        Date::class -> Date().apply { time = getLong(name) }
        else -> throw TypeNotPresentException(T::class.java.simpleName, null)
    }
} as ReadOnlyProperty<FragmentActivity, T>

