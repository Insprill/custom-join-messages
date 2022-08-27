package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.files.YamlFile
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.logging.Level

class ActionbarMessage(private val plugin: CustomJoinMessages) : MessageType {

    private lateinit var classCraftPlayer: Class<*>
    private lateinit var classPacketPlayOutChat: Class<*>
    private lateinit var classChatComponentText: Class<*>
    private lateinit var classIChatBaseComponent: Class<*>
    private lateinit var methodGetHandle: Method
    private lateinit var methodSendPacket: Method
    private lateinit var fieldPlayerConnection: Field

    init {
        if (MinecraftVersion.isOlderThan(MinecraftVersion.v1_9_0)) {
            val nmsVersion = "v1_8_R3"
            val nmsPackage = "net.minecraft.server.$nmsVersion"
            classCraftPlayer = Class.forName("org.bukkit.craftbukkit.$nmsVersion.entity.CraftPlayer")
            classPacketPlayOutChat = Class.forName("$nmsPackage.PacketPlayOutChat")
            val classPacket = Class.forName("$nmsPackage.Packet")
            classChatComponentText = Class.forName("$nmsPackage.ChatComponentText")
            classIChatBaseComponent = Class.forName("$nmsPackage.IChatBaseComponent")
            val classPlayerConnection = Class.forName("$nmsPackage.PlayerConnection")
            val classEntityPlayer = Class.forName("$nmsPackage.EntityPlayer")
            methodGetHandle = classCraftPlayer.getDeclaredMethod("getHandle")
            methodSendPacket = classPlayerConnection.getDeclaredMethod("sendPacket", classPacket)
            fieldPlayerConnection = classEntityPlayer.getDeclaredField("playerConnection")
        }
    }

    override val config = YamlFile("messages" + File.separator + "actionbar.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "actionbar"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        var msg = config.getString("$chosenPath.Message")!!
        msg = fillPlaceholders(primaryPlayer, msg)
        for (p in players) {
            if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_9_0)) {
                sendActionbar(msg, p)
            } else {
                sendLegacyActionbar(msg, p)
            }
        }
    }

    private fun sendActionbar(msg: String, player: Player) {
        if (config.getBoolean("MiniMessage") && MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            val component = MiniMessage.miniMessage().deserialize(msg)
            player.sendActionBar(component)
        } else {
            @Suppress("DEPRECATION") // Legacy support
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(msg))
        }
    }

    private fun sendLegacyActionbar(msg: String, player: Player) {
        try {
            val chatComponentText = classChatComponentText.getConstructor(String::class.java).newInstance(msg)
            val chatPacket =
                classPacketPlayOutChat.getConstructor(classIChatBaseComponent, java.lang.Byte.TYPE).newInstance(chatComponentText, 2.toByte())
            val craftPlayer = classCraftPlayer.cast(player)
            val entityPlayer = methodGetHandle.invoke(craftPlayer)
            val playerConnection = fieldPlayerConnection[entityPlayer]
            methodSendPacket.invoke(playerConnection, chatPacket)
        } catch (ex: ReflectiveOperationException) {
            plugin.logger.log(Level.SEVERE, "Failed to send actionbar message!", ex)
        }
    }

}
