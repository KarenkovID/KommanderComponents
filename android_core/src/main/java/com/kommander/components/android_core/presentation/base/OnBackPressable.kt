package com.kommander.components.android_core.presentation.base

/**
 * Date: 30/08/17
 *
 * @author Alexander Blinov
 */
interface OnBackPressable {

    fun addOnBackPressedListener(listener: OnBackPressedListener)

    fun removeOnBackPressedListener(listener: OnBackPressedListener)

    fun performOnBackPressed(): Boolean

}