package com.kommander.components.android.navigation

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.kommander.components.android.presentation.base.InnerScreensStack
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace

class NestedStackScreenNavigator(
        private val activity: FragmentActivity,
        private val fragmentManager: FragmentManager,
        private val containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    // Contains all available screens for tabs
    private val nestedContainers = mutableSetOf<Pair<String, Fragment>>()

    fun registerScreens(screen: SupportAppScreen) {
        nestedContainers.add(screen.screenKey to initContainer(screen.screenKey) { screen.fragment })
    }

    override fun applyCommands(commands: Array<out Command>?) {
        commands?.forEach(this::applyCommand)
    }

    override fun applyCommand(command: Command?) {
        when (command) {
            is Replace -> replaceScreen(command.screen.screenKey, clean = false)
            is Back -> activity.finish()
            else -> throw IllegalArgumentException("Unsupported command $command")
        }
    }

    private fun replaceScreen(screenKey: String, clean: Boolean) {
        val toAttach = nestedContainers.firstOrNull { it.first == screenKey }
                ?: throw IllegalArgumentException("Unknown screen toAttach $screenKey")

        val toDetach = nestedContainers - toAttach

        replace(toDetach = toDetach, toAttach = toAttach)

        val fragmentToAttach = toAttach.second
        if (clean && fragmentToAttach is InnerScreensStack) {
            fragmentToAttach.cleanScreenStack()
        }
    }

    @SuppressLint("CommitTransaction")
    private fun replace(toDetach: Collection<Pair<String, Fragment>>, toAttach: Pair<String, Fragment>) {
        with(fragmentManager.beginTransaction()) {
            toDetach.forEach { detach(it.second) }
            attach(toAttach.second)
            commitNow()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Fragment> initContainer(tag: String, factory: (Any?) -> T): T {
        var fragment = fragmentManager.findFragmentByTag(tag) as T?
        if (fragment == null) {
            fragment = factory.invoke(null)

            fragmentManager
                    .beginTransaction()
                    .add(containerId, fragment, tag)
                    .detach(fragment)
                    .commitNow()
        }
        return fragment
    }

}
