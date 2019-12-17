package com.kommander.components.android_core.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.round

val Int.px
    get() = toFloat().px

val Int.spToPx
    get() = toFloat().spToPx

val Int.spToPxInt
    get() = toFloat().spToPx.toInt()

val Int.pxInt
    get() = this.px.toInt()

val Float.px
    get() = round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
    )

val Float.spToPx
    get() = round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )
    )

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this,
        Resources.getSystem().displayMetrics
    )