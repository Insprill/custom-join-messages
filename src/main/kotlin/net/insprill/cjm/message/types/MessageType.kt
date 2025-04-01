package net.insprill.cjm.message.types

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ConfigSettings
import de.leonhard.storage.internal.settings.DataType
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import org.bukkit.entity.Player
import java.nio.file.Paths

abstract class MessageType(
    plugin: CustomJoinMessages,
    /**
     * The name of the message type.
     */
    val name: String,
    /**
     * The primary key that all messages are under.
     */
    val key: String
) {

    /**
     * @return The [FlatFile] associated with the message type.
     */
    val config: FlatFile = SimplixBuilder.fromPath(Paths.get("${plugin.dataFolder}/messages/$name.yml"))
        .addInputStreamFromResource("messages/$name.yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .reloadCallback {
            // Don't allow reloading from within the reload callback.
            // https://github.com/Insprill/custom-join-messages/issues/65
            val prevReloadSettings = it.reloadSettings
            it.reloadSettings = ReloadSettings.MANUALLY
            try {
                plugin.messageSender.reloadPermissions(it)
            } finally {
                it.reloadSettings = prevReloadSettings
            }
        }
        .createYaml()

    /**
     * @return Whether this MessageType is enabled.
     */
    open val isEnabled: Boolean
        get() = config.getBoolean("Enabled")

    /**
     * Called when a message should be sent.
     *
     * @param primaryPlayer Player joining/ leaving.
     * @param recipients       Everyone who should receive the message.
     * @param chosenPath    Full path to the chosen message.
     * @param visibility    The visibility of the message.
     */
    abstract fun handle(
        primaryPlayer: Player,
        recipients: List<Player>,
        chosenPath: String,
        visibility: MessageVisibility
    )

}
