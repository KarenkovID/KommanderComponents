package com.kommander.components.domain_core.reflection

import java.lang.reflect.Method

class PrivateMethod<TClass, TReturnValue>(
        clazz: Class<TClass>,
        methodName: String,
        private val fieldObject: TClass,
        vararg classes: Class<out Any?>
) {

    private val method: Method = clazz.getDeclaredMethod(methodName, *classes).also { it.isAccessible = true }

    operator fun invoke(vararg args: Any?): TReturnValue = method.invoke(fieldObject, *args) as TReturnValue

}