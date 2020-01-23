package com.kommander.components.android.recycler.stackLayoutManager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.Keep
import androidx.annotation.Px
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.kommander.components.android.extensions.px
import timber.log.Timber
import java.lang.reflect.InvocationTargetException
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

@Keep
class StackLayoutManager(
        config: Config = Config(),
        private var stackItemsCallback: StackItemsCallback? = null
) : RecyclerView.LayoutManager() {

    companion object {

        private const val NO_PREV_OFFSET = -1

        private const val MAX_VELOCITY = 14000f

    }

    @Suppress("PrivatePropertyName", "detekt.VariableNaming", "detekt.MagicNumber")
    private val DP_PER_SECOND_UNIT = (1000 / 1.px).toInt()

    @Px
    private val stackItemsSpace: Int = config.stackItemsSpace
    @Px
    private val outItemsSpace: Int = config.outItemsSpace
    @Px
    private val anchorViewBaseLine: Int = config.anchorViewBaseLine
    @Px
    private val scaleRatio: Float = config.scaleRatio
    private val parallax: Float = config.parallax
    private val animationDurationMs = config.animationDurationMs

    @Px
    private var itemSizeWithSpace: Int = 0
    @Px
    private var itemWidth: Int = 0
    @Px
    private var totalOffset: Int = 0
    // the counting variable ,record the total offset including parallax
    @Px
    private var prevTotalOffset: Int = NO_PREV_OFFSET
    private var animator: ObjectAnimator? = null
    private var lastAnimateValue: Int = 0
    private var initial: Boolean = false
    private var minVelocityX: Int = 0

    private var recycler: RecyclerView.Recycler? = null
    private var recyclerView by Delegates.observable<RecyclerView?>(null) { _, _, newValue ->
        newValue?.setChildDrawingOrderCallback { childCount: Int, i: Int ->
            childCount - i - 1
        }
    }

    private val methodSetScrollState by lazy {
        RecyclerView::class.java.getDeclaredMethod("setScrollState", Int::class.javaPrimitiveType)
    }

    private val touchListener = OnTouchListener()

    private val onFlingListener = OnFlingListener()

    // This field is used for ObjectAnimator
    @Suppress("Unused")
    var animateValue: Int = 0
        set(animateValue) {
            field = animateValue
            val dy = this.animateValue - lastAnimateValue
            fill(recycler!!, dy, false)
            lastAnimateValue = animateValue
        }

    override fun canScrollHorizontally() = true

    override fun canScrollVertically() = false

    override fun isAutoMeasureEnabled(): Boolean = true

    override fun generateDefaultLayoutParams() =
            RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int =
            fill(recycler, dx)

    override fun onLayoutCompleted(state: RecyclerView.State) {
        super.onLayoutCompleted(state)
        if (itemCount <= 0) {
            return
        }
        if (!initial) {
            fill(recycler!!, 0, false)
            initial = true
        }
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        initial = false
        prevTotalOffset = NO_PREV_OFFSET
        totalOffset = 0
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
        view.setOnTouchListener(touchListener)
        view.onFlingListener = onFlingListener
    }

    override fun scrollToPosition(position: Int) {
        if (position > itemCount - 1) {
            Timber.d("position is $position but itemCount is $itemCount")
            return
        }
        val currPosition = getAnchorItemPos()
        val distance = (position - currPosition) * itemSizeWithSpace
        val dur = computeSettleDuration(abs(distance), 0f)
        createAndStartAnimator(dur, distance)
    }

    override fun requestLayout() {
        super.requestLayout()
        initial = false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount <= 0) {
            return
        }

        this.recycler = recycler
        fill(recycler, 0)
    }

    fun getScale(position: Int): Float {
        val currPos = getAnchorItemPos()
        val n = totalOffset.toFloat() / itemSizeWithSpace
        return if (position <= currPos) {
            1f
        } else {
            1f - scaleRatio * (position - n)
        }
    }

    private fun fill(recycler: RecyclerView.Recycler, rawDx: Int, applyParallax: Boolean = true): Int {
        val dx = calculatePossibleDx(rawDx, applyParallax)
        when {
            totalOffset + dx < 0 ||
                    totalOffset + dx == prevTotalOffset
                    || (totalOffset.toFloat() + dx.toFloat()) / itemSizeWithSpace > itemCount - 1 -> {
                return 0
            }
            itemWidth == 0 -> {
                // got the itemSizeWithSpace basing on the first child,of course we assume that  all the item has the same size
                detachAndScrapAttachedViews(recycler)
                val anchorView = recycler.getViewForPosition(0)
                measureChildWithMargins(anchorView, 0, 0)
                itemWidth = anchorView.measuredWidth
                itemSizeWithSpace = itemWidth + outItemsSpace
                // because this method will be called twice
                minVelocityX = ViewConfiguration.get(anchorView.context).scaledMinimumFlingVelocity
            }
        }

        detachAndScrapAttachedViews(recycler)
        totalOffset += dx
        recycleViews(recycler, dx)

        val currPos = getAnchorItemPos()
        val curPosLeft = left(currPos)
        val leavingSpace = width - (curPosLeft + itemWidth)

        val start = max(0, currPos - curPosLeft / itemSizeWithSpace - 2)
        val end = if (stackItemsSpace == 0) {
            itemCount - 1
        } else {
            min(itemCount - 1, currPos + leavingSpace / stackItemsSpace + 1)
        }
        // layout view
        for (i in start..end) {
            val view = recycler.getViewForPosition(i)

            addView(view)
            measureChildWithMargins(view, 0, 0)

            val scale = getScale(i)
            val left = (left(i) + (1 - scale) * view.measuredWidth / 2).toInt()
            val top = 0
            val right = left + view.measuredWidth
            val bottom = top + view.measuredHeight

            layoutDecoratedWithMargins(view, left, top, right, bottom)
            view.scaleY = scale
            view.scaleX = scale
        }
        onOffsetChanged(prevTotalOffset, totalOffset)
        prevTotalOffset = totalOffset
        return dx
    }

    private fun calculatePossibleDx(rawDx: Int, applyParallax: Boolean = true): Int {
        val dx = if (applyParallax) (rawDx * parallax).toInt() else rawDx
        val newTotalOffset = totalOffset + dx
        return when {
            newTotalOffset < 0 -> dx - newTotalOffset
            itemSizeWithSpace > 0 && newTotalOffset.toFloat() / itemSizeWithSpace > itemCount - 1 -> {
                dx - newTotalOffset % itemSizeWithSpace
            }
            else -> dx
        }
    }

    private fun recycleViews(recycler: RecyclerView.Recycler, dx: Int) {
        val count = childCount
        // remove and recycle views
        for (i in 0 until count) {
            val child = getChildAt(i)!!
            if (recycleHorizontally(child, dx)) {
                removeAndRecycleView(child, recycler)
            }
        }
    }

    private fun getAnchorItemPos() = totalOffset / itemSizeWithSpace

    private fun getOffsetFromAnchor() = totalOffset % itemSizeWithSpace

    /**
     * @param position the index of the item in the adapter
     * @return the accurate left position for the given item
     */
    private fun left(position: Int): Int {
        val currPos = getAnchorItemPos()
        val offsetFromAnchor = getOffsetFromAnchor()
        val relativeOffset = offsetFromAnchor.toFloat() / itemSizeWithSpace

        val left = when {
            // current
            position == currPos -> anchorViewBaseLine - relativeOffset * itemSizeWithSpace
            // out of stack
            position < currPos -> anchorViewBaseLine - (itemWidth + outItemsSpace) * (currPos - position + relativeOffset)
            // in stack
            else -> anchorViewBaseLine + stackItemsSpace * (position - currPos - relativeOffset)
        }
        Timber.d("position $position left $left currPos $currPos tail $offsetFromAnchor x $relativeOffset")
        return left.toInt()
    }

    @Suppress("detekt.MagicNumber")
    private fun computeSettleDuration(distance: Int, xvel: Float): Int {
        val sWeight = 0.5f * distance / itemSizeWithSpace
        val velWeight = if (xvel > 0) 0.5f * minVelocityX / xvel else 0f

        return ((sWeight + velWeight) * animationDurationMs).toInt()
    }

    private fun createAndStartAnimator(dur: Int, finalXorY: Int) {
        animator = ObjectAnimator.ofInt(
                this@StackLayoutManager,
                "animateValue",
                0,
                finalXorY
        ).apply {
            duration = dur.toLong()
            interpolator = FastOutSlowInInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    lastAnimateValue = 0
                }

                override fun onAnimationCancel(animation: Animator) {
                    lastAnimateValue = 0
                }

            })
            start()
        }
    }

    /**
     * we need to set scrollstate to [RecyclerView.SCROLL_STATE_IDLE] idle
     * stop RV from intercepting the touch event which block the item click
     */
    private fun setScrollStateIdle() {
        try {
            methodSetScrollState.isAccessible = true
            methodSetScrollState.invoke(recyclerView, RecyclerView.SCROLL_STATE_IDLE)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

    private fun recycleHorizontally(view: View?, dy: Int): Boolean =
            view != null && (view.left - dy < 0 || view.right - dy > width)

    private fun absMax(a: Int, b: Int): Int = if (abs(a) > abs(b)) a else b

    private fun onOffsetChanged(prevTotalOffset: Int, curTotalOffset: Int) {
        if (stackItemsCallback == null) {
            return
        }
        val prevRelativePos = prevTotalOffset.toFloat() / itemSizeWithSpace
        val curRelativePos = curTotalOffset.toFloat() / itemSizeWithSpace
        val prevLastShowed = ceil(prevRelativePos).toInt().coerceIn(0 until itemCount)
        val currLastShowed = ceil(curRelativePos).toInt().coerceIn(0 until itemCount)
        stackItemsCallback?.onRelativePositionChanged(curRelativePos)
        if (prevLastShowed == currLastShowed) {
            return
        }
        // scroll to left
        if (prevLastShowed < currLastShowed) {
            for (pos in prevLastShowed + 1..currLastShowed) {
                stackItemsCallback?.onItemShowedInStack(pos)
            }
        } else {
            for (pos in currLastShowed + 1..prevLastShowed) {
                stackItemsCallback?.onItemHidedInStack(pos)
            }
        }
    }

    private inner class OnTouchListener : View.OnTouchListener {

        private val velocityTracker = VelocityTracker.obtain()
        private var pointerId: Int = 0

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            if (itemCount == 0) {
                return false
            }
            velocityTracker.addMovement(event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (animator?.isRunning == true) {
                    animator?.cancel()
                }
                pointerId = event.getPointerId(0)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                if (view.isPressed) {
                    view.performClick()
                }
                velocityTracker.computeCurrentVelocity(DP_PER_SECOND_UNIT, MAX_VELOCITY)
                val xVelocity = velocityTracker.getXVelocity(pointerId)
                val offsetFromAnchor = getOffsetFromAnchor()
                if (abs(xVelocity) < minVelocityX && offsetFromAnchor != 0) {
                    val scrollX = -offsetFromAnchor + if (offsetFromAnchor >= itemSizeWithSpace / 2) itemSizeWithSpace else 0
                    Timber.d("onTouch: ===CREATE AND START ANIMATION===")
                    createAndStartAnimator(
                            (abs(scrollX.toFloat() / itemSizeWithSpace) * animationDurationMs).toInt(),
                            scrollX
                    )
                }
            }
            return false
        }

    }

    private inner class OnFlingListener : RecyclerView.OnFlingListener() {

        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            if (itemCount == 0) {
                return true
            }
            val offsetFromAnchorLeft = getOffsetFromAnchor()
            val offsetFromAnchorRight = itemSizeWithSpace - offsetFromAnchorLeft
            val velocity = absMax(velocityX, velocityY)
            val scrollX = if (velocity > 0) offsetFromAnchorRight else -offsetFromAnchorLeft
            val duration = computeSettleDuration(abs(scrollX), abs(velocity).toFloat())
            createAndStartAnimator(duration, scrollX)
            setScrollStateIdle()
            return true
        }

    }

    interface StackItemsCallback {

        fun onItemShowedInStack(adapterItemPosition: Int) = Unit

        fun onItemHidedInStack(adapterItemPosition: Int) = Unit

        fun onRelativePositionChanged(relativeItemPosition: Float) = Unit

    }

}