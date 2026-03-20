package it.marcozanetti.androidapilevels.ui

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
        36 to ".ui.MainActivityApi36Alias"
    )

    private val allAliases = listOf(DEFAULT_ALIAS) + supportedAliases.values

    fun updateLauncherIcon(
        context: Context,
        launchedComponentClassName: String? = null
    ) {
        val targetAlias = supportedAliases[Build.VERSION.SDK_INT] ?: DEFAULT_ALIAS
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val previousAlias = prefs.getString(PREF_LAST_ALIAS, DEFAULT_ALIAS)
        val keepEnabledAlias = launchedComponentClassName
            ?.takeIf { launchedClass -> allAliases.any { alias -> launchedClass == context.packageName + alias } }
            ?.removePrefix(context.packageName)

        if (previousAlias == targetAlias && isAliasEnabled(context, targetAlias)) {
            // On some launchers/IDE runs the app can still be started via a previous alias.
            // Keep that alias enabled for this run so explicit launches do not fail.
            if (keepEnabledAlias != null && keepEnabledAlias != targetAlias) {
                setAliasState(context, keepEnabledAlias, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            }
            return
        }

        setAliasState(context, targetAlias, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        allAliases
            .asSequence()
            .filter { alias ->
                alias != targetAlias && alias != keepEnabledAlias
            }
            .forEach { alias ->
                setAliasState(context, alias, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
            }

        prefs.edit {
            putString(PREF_LAST_ALIAS, targetAlias)
        }
    }

    private fun isAliasEnabled(context: Context, aliasName: String): Boolean {
        val state = context.packageManager.getComponentEnabledSetting(componentName(context, aliasName))
        return when (state) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> aliasName == DEFAULT_ALIAS
            else -> false
        }
    }

    private fun setAliasState(context: Context, aliasName: String, newState: Int) {
        val componentName = componentName(context, aliasName)
        val packageManager = context.packageManager
        if (packageManager.getComponentEnabledSetting(componentName) != newState) {
            packageManager.setComponentEnabledSetting(
                componentName,
                newState,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun componentName(context: Context, aliasName: String): ComponentName {
        return ComponentName(context.packageName, context.packageName + aliasName)
    }
}


