@file:Suppress("DEPRECATION")

package net.insprill.cjm.formatting

import net.md_5.bungee.api.chat.TextComponent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

class LegacyFormatterTest {

    private lateinit var server: ServerMock
    private lateinit var formatter: Formatter

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        formatter = LegacyFormatter()
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
    fun format_LegacyColorCodes() {
        assertEquals("§aHello, §b§lworld!", format("&aHello, &b&lworld!"))
    }

    @Test
    fun format_HexCodes() {
        val expected = "§x§0§0§a§f§5§cHowdy!"
        assertEquals(expected, format("{#00af5c}Howdy!"))
        assertEquals(expected, format("<#00af5c>Howdy!"))
        assertEquals(expected, format("&#00af5cHowdy!"))
    }

    private fun format(string: String): String {
        return TextComponent.toLegacyText(*formatter.format(string))
    }

}
