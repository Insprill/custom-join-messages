@file:Suppress("DEPRECATION")

package net.insprill.cjm.formatting

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.md_5.bungee.api.chat.TextComponent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MiniMessageFormatterTest {

    private lateinit var server: ServerMock
    private lateinit var formatter: Formatter

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        formatter = MiniMessageFormatter()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun format_PlainText() {
        assertEquals("§fHowdy!", format("Howdy!"))
    }

    @Test
    fun format_NamedColors() {
        assertEquals("§f§aHello,§f §b§lworld!", format("<green>Hello,</green> <aqua><bold>world!</bold></aqua>"))
    }

    @Test
    fun format_HexColors() {
        assertEquals("§x§0§0§A§F§5§CHowdy!", format("<#00af5c>Howdy!"))
    }

    @Test
    fun format_LegacyColors() {
        assertEquals("§f§aHello,§f §b§lworld!", format("&aHello,&r &b&lworld!"))
    }

    private fun format(string: String): String {
        return TextComponent.toLegacyText(*formatter.format(string))
    }

}
