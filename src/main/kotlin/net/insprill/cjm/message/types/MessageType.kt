package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.xenlib.files.YamlFile
import org.bukkit.entity.Player

interface MessageType {
    /**
     * @return The name of the message type.
     */
    val name: String

    /**
     * @return The YamlFile associated with the message type.
     */
    val config: YamlFile

    /**
     * @return Whether this MessageType is enabled.
     */
    val isEnabled: Boolean
        get() = config.getBoolean("Enabled")

    /**
     * @return The primary key that all messages are under.
     */
    val key: String

    /**
     * Called when a message should be sent.
     *
     * @param primaryPlayer Player joining/ leaving.
     * @param players       Everyone who should receive the message.
     * @param rootPath      Root path of the chosen message.
     * @param chosenPath    Full path to the chosen message.
     * @param visibility    The visibility of the message.
     */
    fun handle(
        primaryPlayer: Player,
        players: List<Player>,
        rootPath: String?,
        chosenPath: String,
        visibility: MessageVisibility
    )
}
