package com.kommander.components.android.recycler

import android.animation.Animator
import androidx.annotation.Px
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kommander.components.android.extensions.pxInt
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

open class DraggableItemCallback(
        private val onItemMoveListener: (oldPosition: Int, newPosition: Int) -> Unit,
        private val canMoveItem: (itemPosition: Int) -> Boolean,
        private val canDropOver: (oldPosition: Int, newPosition: Int) -> Boolean,
        private val onItemReleaseListener: (oldPosition: Int, newPosition: Int) -> Unit,
        private val dragStartAnimator: Animator? = null,
        private val dragEndAnimator: Animator? = null,
        @Px private val autoScrollBoundsOffset: Int = 0,
        @Suppress("detekt.MagicNumber")
        @Px private val maxDragScroll: Int = 20.pxInt
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    private var startDragPosition = -1

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        onItemMoveListener(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun canDropOver(
            recyclerView: RecyclerView,
            current: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean = canDropOver(current.adapterPosition, target.adapterPosition)

    override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ) = ItemTouchHelper.Callback.makeMovementFlags(
            if (canMoveItem(viewHolder.adapterPosition)) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            0
    )

    override fun onSelectedChanged(
            viewHolder: RecyclerView.ViewHolder?,
            actionState: Int
    ) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder?.itemView != null) {
            startDragPosition = viewHolder.adapterPosition
            dragStartAnimator?.apply {
                setTarget(viewHolder.itemView)
                start()
            }
        }
    }

    override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
        val targetPosition = viewHolder.adapterPosition
        if (startDragPosition != -1 && targetPosition != -1) {
            onItemReleaseListener(startDragPosition, viewHolder.adapterPosition)
        } else {
            Timber.log(
                    1,
                    IllegalStateException(
                            "From position or target position cannot be -1.\n" +
                            "startDragPosition: $startDragPosition, targetPosition: $targetPosition"
                    )
            )
        }
        startDragPosition = -1
        dragEndAnimator?.apply {
            setTarget(viewHolder.itemView)
            start()
        }
    }

    override fun interpolateOutOfBoundsScroll(
            recyclerView: RecyclerView,
            viewSize: Int,
            viewSizeOutOfBounds: Int,
            totalSize: Int,
            msSinceStartScroll: Long
    ): Int {
        val direction = sign(viewSizeOutOfBounds.toFloat()).toInt()
        val outOfBoundsRatio = min(1f, abs(viewSizeOutOfBounds).toFloat() / autoScrollBoundsOffset)
        val scrollValue = (direction * maxDragScroll * outOfBoundsRatio).toInt()
        return if (scrollValue == 0) {
            sign(viewSizeOutOfBounds.toDouble()).toInt()
        } else {
            scrollValue
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun isItemViewSwipeEnabled() = false

}