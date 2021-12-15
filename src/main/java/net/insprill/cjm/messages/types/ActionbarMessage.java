package net.insprill.cjm.messages.types;

import net.insprill.cjm.utils.ChatUtils;
import net.insprill.xenlib.MinecraftVersion;
import net.insprill.xenlib.files.YamlFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ActionbarMessage implements MessageType {

    private final YamlFile config = new YamlFile("messages" + File.separator + "actionbar.yml", false);

    // Reflection for 1.8.x.
    private static Class<?> classCraftPlayer = null;
    private static Class<?> classPacketPlayOutChat = null;
    private static Class<?> classChatComponentText = null;
    private static Class<?> classIChatBaseComponent = null;
    private static Method methodGetHandle = null;
    private static Method methodSendPacket = null;
    private static Field fieldPlayerConnection = null;

    static {
        if (!MinecraftVersion.isAtLeast(MinecraftVersion.v1_9_R1)) {
            String nmsVersion = "v1_8_R3";
            String nmsPackage = "net.minecraft.server." + nmsVersion;
            try {
                classCraftPlayer = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
                classPacketPlayOutChat = Class.forName(nmsPackage + ".PacketPlayOutChat");
                Class<?> classPacket = Class.forName(nmsPackage + ".Packet");
                classChatComponentText = Class.forName(nmsPackage + ".ChatComponentText");
                classIChatBaseComponent = Class.forName(nmsPackage + ".IChatBaseComponent");
                Class<?> classPlayerConnection = Class.forName(nmsPackage + ".PlayerConnection");
                Class<?> classEntityPlayer = Class.forName(nmsPackage + ".EntityPlayer");

                methodGetHandle = classCraftPlayer.getDeclaredMethod("getHandle");
                methodSendPacket = classPlayerConnection.getDeclaredMethod("sendPacket", classPacket);

                fieldPlayerConnection = classEntityPlayer.getDeclaredField("playerConnection");
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public YamlFile getConfig() {
        return config;
    }

    @Override
    public String getKey() {
        return "Messages";
    }

    @Override
    public String getName() {
        return "actionbar";
    }

    @Override
    public void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath) {
        String msg = config.getString(chosenPath + ".Message");
        msg = ChatUtils.setPlaceholders(primaryPlayer, msg);
        for (Player p : players) {
            sendActionBar(p, msg);
        }
    }

    /**
     * Sends an Action Bar messages to a player.
     *
     * @param player  Player to send message to.
     * @param message Message to send (unformatted).
     */
    public void sendActionBar(Player player, String message) {
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_9_R1)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        } else {
            try {
                Object chatComponentText = classChatComponentText.getConstructor(new Class[]{ String.class }).newInstance(message);
                Object chatPacket = classPacketPlayOutChat.getConstructor(new Class[]{ classIChatBaseComponent, Byte.TYPE }).newInstance(chatComponentText, (byte) 2);
                Object craftPlayer = classCraftPlayer.cast(player);
                Object entityPlayer = methodGetHandle.invoke(craftPlayer);
                Object playerConnection = fieldPlayerConnection.get(entityPlayer);
                methodSendPacket.invoke(playerConnection, chatPacket);
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

}
