package com.kommander.components.android_core.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

open class AppRouter : Router() {

    fun backToAndNavigateTo(backScreen: Screen, navigateToScreenKey: Screen) {
        executeCommands(BackTo(backScreen), Forward(navigateToScreenKey))
    }

    fun backAndReplace(replaceScreen: Screen) {
        executeCommands(Back(), Replace(replaceScreen))
    }

    fun backToAndReplace(backScreenKey: Screen, replaceScreenKey: Screen) {
        executeCommands(BackTo(backScreenKey), Replace(replaceScreenKey))
    }

    fun backInclusive(screenKey: Screen) {
        executeCommands(BackTo(screenKey), Back())
    }

    fun newScreenStack(screens: List<Screen>) {
        executeCommands(listOf(BackTo(null)) + createForawrdCommands(screens))
    }

    fun multipleForward(screens: List<Screen>) {
        executeCommands(createForawrdCommands(screens))
    }

    private fun createForawrdCommands(screens: List<Screen>): List<Command> {
        return screens.fold(mutableListOf()) { acc, screen ->
            acc.also { it.add(Forward(screen)) }
        }
    }

    fun addScreen(screen: Screen) {
        executeCommands(Forward(screen))
    }
//
//    fun replaceAndResetScreen(screenKey: String, data: Any? = null) {
//        executeCommands(ReplaceAndReset(screenKey, data))
//    }

    @Suppress("detekt.SpreadOperator")
    fun back(deep: Int) {
        if (deep == 1) {
            exit()
        } else if (deep > 1) {
            executeCommands(deep * { Back() })
        }
    }

//    fun backWithResult(resultCode: Int, result: Any?, deep: Int) {
//        if (deep == 1) {
//            exitWithResult(resultCode, result)
//        } else if (deep > 1) {
//            back(deep)
//            sendResult(resultCode, result)
//        }
//    }

//    fun backToWithResult(screenKey: String, resultCode: Int, result: Any?) {
//        executeCommands(BackTo(screenKey))
//        sendResult(resultCode, result)
//    }

    @Suppress("detekt.SpreadOperator")
    private fun executeCommands(commands: List<Command>) {
        executeCommands(*commands.toTypedArray())
    }

    private operator fun <T> Int.times(provider: () -> T): List<T> {
        if (this < 1) return emptyList()
        if (this == 1) return listOf(provider.invoke())

        return (0 until this).fold(mutableListOf()) { acc, _ ->
            acc.also { it.add(provider.invoke()) }
        }
    }

}
