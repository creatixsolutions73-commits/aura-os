package com.aura.os.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.aura.os.commands.Command
import com.aura.os.commands.CommandResult
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DeviceControlManager(private val context: Context) {

    fun execute(command: Command): CommandResult = when (command) {
        is Command.OpenApplication -> openApp(command.appName)
        Command.ReadBattery -> readBattery()
        Command.ReadTime -> CommandResult.Success(
            "It is " + LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"))
        )
        is Command.Unrecognized -> CommandResult.Unknown("I didn't understand that command yet.")
    }

    private fun openApp(name: String): CommandResult {
        val pm = context.packageManager
        val query = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val match = pm.queryIntentActivities(query, 0)
            .map { it to it.loadLabel(pm).toString() }
            .filter { (_, label) -> label.contains(name, ignoreCase = true) }
            .minByOrNull { (_, label) -> label.length }
            ?: return CommandResult.Failure("I couldn't find an installed app called $name.")
        val launch = pm.getLaunchIntentForPackage(match.first.activityInfo.packageName)
            ?: return CommandResult.Failure("${match.second} has no launchable screen.")
        return try {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launch)
            CommandResult.Success("Opening ${match.second}.")
        } catch (e: Exception) {
            CommandResult.Failure("Android blocked launching ${match.second}.")
        }
    }

    private fun readBattery(): CommandResult {
        val i = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            ?: return CommandResult.Failure("Battery information is unavailable.")
        val level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        if (level < 0 || scale <= 0) return CommandResult.Failure("Battery information is unavailable.")
        return CommandResult.Success("Battery is at ${level * 100 / scale} percent.")
    }
}
