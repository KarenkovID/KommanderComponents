package com.kommander.components.android_core.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

open class FragmentScreen(
        private val innerScreenKey: String,
        private val factory: () -> Fragment
) : SupportAppScreen() {

    override fun getScreenKey() = innerScreenKey

    override fun getFragment() = factory()

}