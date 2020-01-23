package com.kommander.components.android.presentation.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import java.util.LinkedHashSet

abstract class BaseActivity : AppCompatActivity, OnBackPressable {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var childBackPressedListeners: MutableSet<OnBackPressedListener> = LinkedHashSet()

    final override fun onBackPressed() {
        val isConsumed = childBackPressedListeners.toList().lastOrNull(OnBackPressedListener::onBackPressed) != null
        if (!isConsumed) {
            performOnBackPressed()
        }
    }

    override fun addOnBackPressedListener(listener: OnBackPressedListener) {
        childBackPressedListeners.add(listener)
    }

    override fun removeOnBackPressedListener(listener: OnBackPressedListener) {
        childBackPressedListeners.remove(listener)
    }

}