package com.kommander.components.domain_core.reflection

import java.lang.reflect.Field
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrivateField<TClass, TField>(
        clazz: Class<TClass>,
        fieldName: String,
        private val fieldObject: TClass
) : ReadWriteProperty<TClass, TField> {

    private val field: Field = clazz.getDeclaredField(fieldName).also { it.isAccessible = true }

    override fun getValue(
            thisRef: TClass,
            property: KProperty<*>
    ): TField = field.get(fieldObject) as TField

    override fun setValue(
            thisRef: TClass,
            property: KProperty<*>,
            value: TField
    ) = field.set(fieldObject, value)

}