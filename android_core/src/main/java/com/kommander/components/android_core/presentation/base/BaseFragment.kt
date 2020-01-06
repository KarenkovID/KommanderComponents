package com.kommander.components.android_core.presentation.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import java.util.LinkedHashSet

abstract class BaseFragment : Fragment, OnBackPressable, OnBackPressedListener {

    constructor(@LayoutRes contentLayoutId: Int): super(contentLayoutId)

    constructor(): super()

    private var childBackPressedListeners: MutableSet<OnBackPressedListener> = LinkedHashSet()

    override fun onResume() {
        super.onResume()
        getParentOnBackPressable().addOnBackPressedListener(this)

    }

    override fun onPause() {
//        activity?.hideKeyboard()
        getParentOnBackPressable().removeOnBackPressedListener(this)
        super.onPause()
    }

    override fun addOnBackPressedListener(listener: OnBackPressedListener) {
        childBackPressedListeners.add(listener)
    }

    override fun removeOnBackPressedListener(listener: OnBackPressedListener) {
        childBackPressedListeners.remove(listener)
    }

    override fun onBackPressed(): Boolean = when {
        childBackPressedListeners.toList().lastOrNull(OnBackPressedListener::onBackPressed) != null -> true
        else -> performOnBackPressed()
    }

    private fun getParentOnBackPressable(): OnBackPressable = when {
        parentFragment != null -> parentFragment as OnBackPressable
        else -> activity as OnBackPressable
    }

}