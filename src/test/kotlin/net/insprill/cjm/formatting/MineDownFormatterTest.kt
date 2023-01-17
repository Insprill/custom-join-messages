@file:Suppress("DEPRECATION")

package net.insprill.cjm.formatting

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.md_5.bungee.api.chat.TextComponent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MineDownFormatterTest {

    private lateinit var server: ServerMock
    private lateinit var formatter: Formatter

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        formatter = MinedownFormatter()
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
    fun format_ColorCodes() {
        assertEquals("§aHello, §b§lworld!", format("&aHello, &b&lworld!"))
    }

    @Test
    fun format_HexCodes() {
        assertEquals("§x§0§0§a§f§5§cHowdy!", format("&#00af5c&Howdy!"))
    }

    private fun format(string: String): String {
        return TextComponent.toLegacyText(*formatter.format(string))
    }

}
