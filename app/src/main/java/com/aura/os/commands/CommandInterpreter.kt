package com.aura.os.commands

sealed interface Command {
    data class OpenApplication(val appName: String) : Command
    data object ReadBattery : Command
    data object ReadTime : Command
    data class Unrecognized(val raw: String) : Command
}

class CommandInterpreter {
    fun interpret(input: String): Command {
        val text = input.lowercase().trim()
            .removePrefix("hey aura").trim(' ', ',', '.', '?', '!')
        OPEN.matchEntire(text)?.let { return Command.OpenApplication(it.groupValues[1].trim()) }
        if (text.contains("battery")) return Command.ReadBattery
        if (text == "what time is it" || text == "what's the time" || text == "time") return Command.ReadTime
        return Command.Unrecognized(input)
    }

    private companion object {
        val OPEN = Regex("^(?:please )?(?:open|launch|start)\\s+(.+)$")
    }
}
