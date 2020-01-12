package ru.terrakok.cicerone.android.support

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import java.util.LinkedList

class AppNavigator : Navigator {

    private val activity: Activity
    private val fragmentManager: FragmentManager
    private val containerId: Int
    private var localStackCopy: LinkedList<String>? = null

    constructor(activity: FragmentActivity, containerId: Int) : this(activity, activity.supportFragmentManager, containerId)

    constructor(activity: FragmentActivity, fragmentManager: FragmentManager, containerId: Int) {
        this.activity = activity
        this.fragmentManager = fragmentManager
        this.containerId = containerId
    }

    override fun applyCommands(commands: Array<Command?>) {
        fragmentManager.executePendingTransactions()
        //copy stack before apply commands
        copyStackToLocal()
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun copyStackToLocal() {
        localStackCopy = LinkedList()
        val stackSize: Int = fragmentManager.getBackStackEntryCount()
        for (i in 0 until stackSize) {
            localStackCopy!!.add(fragmentManager.getBackStackEntryAt(i).getName()!!)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    fun applyCommand(command: Command?) {
        if (command is Forward) {
            activityForward(command)
        } else if (command is Replace) {
            activityReplace(command)
        } else if (command is BackTo) {
            backTo(command)
        } else if (command is Back) {
            fragmentBack()
        }
    }

    fun activityForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)
        // Start activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }

    protected fun fragmentForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val fragment = createFragment(screen)!!
        if (fragment is DialogFragment) {
            fragment.show(fragmentManager, screen.screenKey)
            return
        }
        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                fragment,
                fragmentTransaction
        )


        fragmentTransaction
                .replace(containerId, fragment)
                .addToBackStack(screen.screenKey)
                .commit()
        localStackCopy!!.add(screen.screenKey)
    }

    fun fragmentBack() {
        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()
        } else {
            activityBack()
        }
    }

    fun activityBack() {
        activity.finish()
    }

    fun activityReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)
        // Replace activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        } else {
            fragmentReplace(command)
        }
    }

    fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val fragment = createFragment(screen)
        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            setupFragmentTransaction(
                    command,
                    fragmentManager.findFragmentById(containerId),
                    fragment,
                    fragmentTransaction
            )
            fragmentTransaction
                    .replace(containerId, fragment!!)
                    .addToBackStack(screen.screenKey)
                    .commit()
            localStackCopy!!.add(screen.screenKey)
        } else {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            setupFragmentTransaction(
                    command,
                    fragmentManager.findFragmentById(containerId),
                    fragment,
                    fragmentTransaction
            )
            fragmentTransaction
                    .replace(containerId, fragment!!)
                    .commit()
        }
    }

    /**
     * Performs [BackTo] command transition
     */
    fun backTo(command: BackTo) {
        if (command.screen == null) {
            backToRoot()
        } else {
            val key = command.screen.screenKey
            val index = localStackCopy!!.indexOf(key)
            val size = localStackCopy!!.size
            if (index != -1) {
                for (i in 1 until size - index) {
                    localStackCopy!!.removeLast()
                }
                fragmentManager.popBackStack(key, 0)
            } else {
                backToUnexisting(command.screen as SupportAppScreen)
            }
        }
    }

    private fun backToRoot() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        localStackCopy!!.clear()
    }

    /**
     * Override this method to setup fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     *
     * @param command             current navigation command. Will be only [Forward] or [Replace]
     * @param currentFragment     current fragment in container
     * (for [Replace] command it will be screen previous in new chain, NOT replaced screen)
     * @param nextFragment        next screen fragment
     * @param fragmentTransaction fragment transaction
     */
    fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction?
    ) {
    }

    /**
     * Override this method to create option for start activity
     *
     * @param command        current navigation command. Will be only [Forward] or [Replace]
     * @param activityIntent activity intent
     * @return transition options
     */
    fun createStartActivityOptions(
            command: Command?,
            activityIntent: Intent?
    ): Bundle? {
        return null
    }

    private fun checkAndStartActivity(
            screen: SupportAppScreen,
            activityIntent: Intent,
            options: Bundle?
    ) { // Check if we can start activity
        if (activityIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    /**
     * Called when there is no activity to open `screenKey`.
     *
     * @param screen         screen
     * @param activityIntent intent passed to start Activity for the `screenKey`
     */
    fun unexistingActivity(screen: SupportAppScreen?, activityIntent: Intent?) { // Do nothing by default
    }

    /**
     * Creates Fragment matching `screenKey`.
     *
     * @param screen screen
     * @return instantiated fragment for the passed screen
     */
    fun createFragment(screen: SupportAppScreen): Fragment? {
        val fragment = screen.fragment
        if (fragment == null) {
            errorWhileCreatingScreen(screen)
        }
        return fragment
    }

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't found it.
     *
     * @param screen screen
     */
    fun backToUnexisting(screen: SupportAppScreen?) {
        backToRoot()
    }

    fun errorWhileCreatingScreen(screen: SupportAppScreen) {
        throw RuntimeException("Can't create a screen: " + screen.screenKey)
    }
}