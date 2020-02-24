@file:Suppress("PackageDirectoryMismatch")

package androidx.recyclerview.widget

import android.graphics.Rect
import androidx.annotation.Px
import com.kommander.components.domain.privateField

class PatchedItemTouchHelper(
        callback: Callback,
        @Px private val autoScrollOverlap: Int
) : ItemTouchHelper(callback) {

    private var dragScrollStartTimeInMs by privateField<ItemTouchHelper, Long>("mDragScrollStartTimeInMs", this)
    private var selectedStartX by privateField<ItemTouchHelper, Float>("mSelectedStartX", this)
    private var selectedStartY by privateField<ItemTouchHelper, Float>("mSelectedStartY", this)

    private val tempRect: Rect by lazy(::Rect)

    override fun scrollIfNecessary(): Boolean {
        if (mSelected == null) {
            dragScrollStartTimeInMs = java.lang.Long.MIN_VALUE
            return false
        }
        val now = System.currentTimeMillis()
        val scrollDuration = if (dragScrollStartTimeInMs == java.lang.Long.MIN_VALUE) 0 else now - dragScrollStartTimeInMs
        val layoutManager = mRecyclerView.layoutManager
        var scrollX = 0
        var scrollY = 0
        layoutManager!!.calculateItemDecorationsForChild(mSelected.itemView, tempRect)
        if (layoutManager.canScrollHorizontally()) {
            val curX = (selectedStartX + mDx).toInt()
            val leftDiff = curX - tempRect.left - applyClipToPadding(mRecyclerView.paddingLeft) - autoScrollOverlap
            if (mDx < 0 && leftDiff < 0) {
                scrollX = leftDiff
            } else if (mDx > 0) {
                val rightDiff = curX + mSelected.itemView.width + tempRect.right -
                        (mRecyclerView.width - applyClipToPadding(mRecyclerView.paddingRight)) + autoScrollOverlap
                if (rightDiff > 0) {
                    scrollX = rightDiff
                }
            }
        }
        if (layoutManager.canScrollVertically()) {
            val curY = (selectedStartY + mDy).toInt()
            val topDiff = curY - tempRect.top - applyClipToPadding(mRecyclerView.paddingTop) - autoScrollOverlap
            if (mDy < 0 && topDiff < 0) {
                scrollY = topDiff
            } else if (mDy > 0) {
                val bottomDiff = curY + mSelected.itemView.height + tempRect.bottom -
                        (mRecyclerView.height - applyClipToPadding(mRecyclerView.paddingBottom)) + autoScrollOverlap
                if (bottomDiff > 0) {
                    scrollY = bottomDiff
                }
            }
        }
        if (scrollX != 0) {
            scrollX = mCallback.interpolateOutOfBoundsScroll(
                    mRecyclerView,
                    mSelected.itemView.width,
                    scrollX,
                    mRecyclerView.width,
                    scrollDuration
            )
        }
        if (scrollY != 0) {
            scrollY = mCallback.interpolateOutOfBoundsScroll(
                    mRecyclerView,
                    mSelected.itemView.height,
                    scrollY,
                    mRecyclerView.height,
                    scrollDuration
            )
        }
        if (scrollX != 0 || scrollY != 0) {
            if (dragScrollStartTimeInMs == java.lang.Long.MIN_VALUE) {
                dragScrollStartTimeInMs = now
            }
            mRecyclerView.scrollBy(scrollX, scrollY)
            return true
        }
        dragScrollStartTimeInMs = java.lang.Long.MIN_VALUE
        return false
    }

    private inline fun applyClipToPadding(padding: Int) = if (mRecyclerView.clipToPadding) padding else 0

}