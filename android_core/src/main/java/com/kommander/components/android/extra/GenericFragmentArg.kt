package com.kommander.components.android.extra

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class GenericFragmentArg<T>(
        private val get: Bundle.(String) -> T,
        private val set: Bundle.(String, T) -> Unit
) : ReadWriteProperty<Fragment, T?> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return if (thisRef.arguments?.containsKey(property.name) == true) {
            get(thisRef.arguments!!, property.name)
        } else {
            null
        }
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        if (value != null) {
            val bundle = thisRef.arguments?.let { thisRef.arguments } ?: Bundle()
            set(bundle, property.name, value)
            thisRef.arguments = bundle
        }
    }
}

class GenericActivityArg<T>(
        private val get: Bundle.(String) -> T
) : ReadOnlyProperty<FragmentActivity, T?> {
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T? {
        val bundle = thisRef.intent.extras
        return if (bundle != null && bundle.containsKey(property.name)) {
            get(bundle, property.name)
        } else {
            null
        }
    }
}