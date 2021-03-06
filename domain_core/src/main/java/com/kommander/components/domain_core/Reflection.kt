package com.kommander.components.domain_core

import com.kommander.components.domain_core.reflection.PrivateField
import com.kommander.components.domain_core.reflection.PrivateMethod
import kotlin.properties.ReadWriteProperty

inline fun <reified TClass, TField> privateField(
    fieldName: String,
    fieldObject: TClass
): ReadWriteProperty<TClass, TField> = PrivateField(TClass::class.java, fieldName, fieldObject)

inline fun <reified TClass, TReturnValue> privateMethod(
    methodName: String,
    methodObject: TClass,
    vararg classes: Class<out Any?>
): PrivateMethod<TClass, TReturnValue> =
    PrivateMethod(TClass::class.java, methodName, methodObject, *classes)