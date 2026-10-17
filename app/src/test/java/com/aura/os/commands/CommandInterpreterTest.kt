package com.aura.os.commands

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommandInterpreterTest {
    private val i = CommandInterpreter()

    @Test fun opensApp() = assertEquals(Command.OpenApplication("whatsapp"), i.interpret("Open WhatsApp"))
    @Test fun stripsWakePhrase() = assertEquals(Command.OpenApplication("free fire"), i.interpret("Hey Aura, open Free Fire."))
    @Test fun battery() = assertEquals(Command.ReadBattery, i.interpret("What's my battery percentage?"))
    @Test fun time() = assertEquals(Command.ReadTime, i.interpret("what time is it"))
    @Test fun unknown() = assertTrue(i.interpret("dance") is Command.Unrecognized)
}
