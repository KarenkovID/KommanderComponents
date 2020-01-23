package com.kommander.components.android.recycler

import androidx.recyclerview.widget.DiffUtil

open class DefaultDiffCallback<M>(val oldItems: List<M>, val newItems: List<M>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Any()

    open fun areItemsTheSame(oldItem: M, newItem: M) = oldItem == newItem

    open fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem

}
