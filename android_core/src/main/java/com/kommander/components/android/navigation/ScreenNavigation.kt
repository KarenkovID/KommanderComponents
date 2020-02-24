package com.kommander.components.android.navigation

open class ScreenNavigation(
        protected val router: AppRouter
) {

//    val resultSubscription = router.resultSubscription

    // Need to store navigations for connecting with navigator
//    private val navs = mutableListOf<ScreenNavigation>()

//    private val screens = mutableListOf<Screen>()

//    fun register(navigator: SupportAppNavigator) {
//        navs.forEach { it.register(navigator) }
////        screens.forEach { navigator.registerScreenSpec(it) }
//    }

    fun back(deep: Int = 1) {
        router.back(deep)
    }

//    fun backWithResult(result: ScreenResult<*>, deep: Int = 1) {
//        router.backWithResult(result.resultCode, result.result, deep)
//    }

//    protected fun <N : ScreenNavigation> nav(
//            navigationProvider: (AppRouter) -> N
//    ): N {
//        val currentNavigation = this
//
//        return navigationProvider.invoke(router).apply {
//            currentNavigation.navs.add(this@apply)
//        }
//    }

}