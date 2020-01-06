package com.kommander.components.android_core.presentation.base

import android.content.Context
import android.os.Bundle
import com.kommander.components.android_core.R
import com.kommander.components.android_core.navigation.ScreenNavigation
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

abstract class BaseScreenContainerFragment : BaseFragment(R.layout.fragment_container), InnerScreensStack {

    private lateinit var navigator: Navigator

    protected abstract val navigation: ScreenNavigation

    protected abstract val navigatorHolder: NavigatorHolder

    protected abstract fun openFirstScreen()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator =
                SupportAppNavigator(
                        requireActivity(),
                        childFragmentManager,
                        R.id.screenContainer
                )
//                        .apply { routesNavigation.register(this) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.screenContainer) == null) {
            openFirstScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun performOnBackPressed(): Boolean =
            if (childFragmentManager.backStackEntryCount == 0) {
                false
            } else {
                navigation.back()
                true
            }

//    override fun onBackPressedInternal() = false

}