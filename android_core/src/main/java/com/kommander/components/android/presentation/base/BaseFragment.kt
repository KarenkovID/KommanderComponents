package com.kommander.components.android.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import java.util.LinkedHashSet

abstract class BaseFragment : Fragment, OnBackPressable, OnBackPressedListener {

    @LayoutRes
    private val contentLayoutId: Int?

    constructor(@LayoutRes contentLayoutId: Int): super() {
        this.contentLayoutId = contentLayoutId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            if (contentLayoutId == null) {
                super.onCreateView(inflater, container, savedInstanceState)
            } else {
                inflater.inflate(contentLayoutId, container, false)
            }

    constructor(): super() {
        contentLayoutId = null
    }

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