package com.kommander.components.android.recycler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.kommander.components.android.extensions.inflateViewWithRoot

open class SimpleAdapterDelegate<TItem : TItemBase, TItemBase : Any>(
        @LayoutRes
        private val viewHolderLayoutRes: Int,
        private val itemClass: Class<TItem>
) : AbsListItemAdapterDelegate<TItem, TItemBase, ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflateViewWithRoot(viewHolderLayoutRes))

    override fun isForViewType(item: TItemBase, items: List<TItemBase>, position: Int): Boolean = item.javaClass == itemClass

    override fun onBindViewHolder(item: TItem, viewHolder: ViewHolder, payloads: List<Any>) =
            onBindViewHolder(item, viewHolder)

    protected open fun onBindViewHolder(item: TItem, viewHolder: ViewHolder) = Unit

}