package com.kommander.components.android_core.recycler.stack_layout_manager

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.Px

data class Config(

        @IntRange(from = 2) @Px
        var stackItemsSpace: Int = 60,

        @Px
        var outItemsSpace: Int = 20,

        @Px
        var anchorViewBaseLine: Int = 100,

        @FloatRange(from = 0.0, to = 1.0)
        var scaleRatio: Float = 0.1f,

        @FloatRange(from = 1.0, to = 2.0)
        val parallax: Float = 1f,

        val animationDurationMs: Int = 300

)
