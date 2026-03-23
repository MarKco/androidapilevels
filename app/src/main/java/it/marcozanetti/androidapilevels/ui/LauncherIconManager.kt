package it.marcozanetti.androidapilevels.ui

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.edit

object LauncherIconManager {
    private const val PREFS_NAME = "dynamic_launcher_icon"
    private const val PREF_LAST_ALIAS = "last_alias"

    private const val DEFAULT_ALIAS = ".ui.MainActivityDefaultAlias"

    private val supportedAliases = linkedMapOf(
        23 to ".ui.MainActivityApi23Alias",
        24 to ".ui.MainActivityApi24Alias",
        25 to ".ui.MainActivityApi25Alias",
        26 to ".ui.MainActivityApi26Alias",
        27 to ".ui.MainActivityApi27Alias",
        28 to ".ui.MainActivityApi28Alias",
        29 to ".ui.MainActivityApi29Alias",
        30 to ".ui.MainActivityApi30Alias",
        31 to ".ui.MainActivityApi31Alias",
        32 to ".ui.MainActivityApi32Alias",
        33 to ".ui.MainActivityApi33Alias",
        34 to ".ui.MainActivityApi34Alias",
        35 to ".ui.MainActivityApi35Alias",
        36 to ".ui.MainActivityApi36Alias",
        37 to ".ui.MainActivityApi37Alias",
        38 to ".ui.MainActivityApi38Alias",
        39 to ".ui.MainActivityApi39Alias",
        40 to ".ui.MainActivityApi40Alias"
    )

    private val allAliases = listOf(DEFAULT_ALIAS) + supportedAliases.values

    /**
     * Updates the launcher icon based on the current API level.
     * To avoid "closing in the face", we enable the new icon immediately but
     * defer disabling the old one until the user leaves the app (onActivityStopped).
     */
    fun updateLauncherIcon(activity: Activity) {
        val context = activity.applicationContext
        val targetAlias = supportedAliases[Build.VERSION.SDK_INT] ?: DEFAULT_ALIAS
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastAlias = prefs.getString(PREF_LAST_ALIAS, null)

        // If the icon is already correct and synced, do nothing
        if (lastAlias == targetAlias && isAliasEnabled(context, targetAlias)) {
            return
        }

        // 1. Enable the correct icon immediately
        setAliasState(context, targetAlias, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)

        // 2. Register a callback to disable old icons only when the app goes to background
        activity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityStopped(a: Activity) {
                if (a === activity) {
                    // User left the app. Now it's safe to disable the previous icons.
                    // This avoids the process kill while the user is actively using the app.
                    allAliases.forEach { alias ->
                        if (alias != targetAlias) {
                            setAliasState(context, alias, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
                        }
                    }
                    prefs.edit { putString(PREF_LAST_ALIAS, targetAlias) }
                    
                    // Cleanup the callback
                    activity.application.unregisterActivityLifecycleCallbacks(this)
                }
            }

            override fun onActivityCreated(a: Activity, b: Bundle?) {}
            override fun onActivityStarted(a: Activity) {}
            override fun onActivityResumed(a: Activity) {}
            override fun onActivityPaused(a: Activity) {}
            override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
            override fun onActivityDestroyed(a: Activity) {}
        })
    }

    private fun isAliasEnabled(context: Context, aliasName: String): Boolean {
        val state = context.packageManager.getComponentEnabledSetting(componentName(context, aliasName))
        return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || 
               (state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT && aliasName == DEFAULT_ALIAS)
    }

    private fun setAliasState(context: Context, aliasName: String, newState: Int) {
        val componentName = componentName(context, aliasName)
        val packageManager = context.packageManager
        try {
            if (packageManager.getComponentEnabledSetting(componentName) != newState) {
                packageManager.setComponentEnabledSetting(
                    componentName,
                    newState,
                    PackageManager.DONT_KILL_APP
                )
            }
        } catch (e: Exception) {
            // Ignore if alias is missing in manifest
        }
    }

    private fun componentName(context: Context, aliasName: String): ComponentName {
        val name = if (aliasName.startsWith(".")) context.packageName + aliasName else aliasName
        return ComponentName(context.packageName, name)
    }
}
