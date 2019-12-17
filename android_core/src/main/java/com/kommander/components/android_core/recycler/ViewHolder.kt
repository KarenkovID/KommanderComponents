package com.kommander.components.android_core.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    override val containerView: View
        get() = itemView

}