package net.insprill.cjm.commands;

import com.google.common.collect.ImmutableMap;
import net.insprill.cjm.handlers.RandomHandler;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import net.insprill.cjm.messages.MessageVisibility;
import net.insprill.cjm.messages.types.MessageType;
import net.insprill.xenlib.XenUtils;
import net.insprill.xenlib.commands.ICommandArgument;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CjmArgPreview implements ICommandArgument {

    @Override
    public String getBaseArg() {
        return "preview";
    }

    @Override
    public String getPermission() {
        return "cjm.command.preview";
    }

    @Override
    public Map<String, Boolean> getSubArgs() {
        return ImmutableMap.of(
                "target", true,
                "type", true,
                "visibility", true,
                "action", true,
                "message id", true
        );
    }

    @Override
    public String getDescription() {
        return "Allows you to preview messages without re-logging.";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 6) {
            Lang.send(sender, "commands.invalid-usage");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            Lang.send(sender, "commands.preview.invalid-player", "%player%;" + args[1]);
            return;
        }

        MessageType messageType = MessageSender.getInstance().getMessageTypes().get(args[2]);
        if (messageType == null) {
            Lang.send(sender, "commands.preview.invalid-message-type", "%type%;" + args[2]);
            return;
        }

        if (!XenUtils.isValidEnum(MessageVisibility.class, args[3])) {
            Lang.send(sender, "commands.preview.invalid-visibility", "%visibility%;" + args[3]);
            return;
        }
        MessageVisibility visibility = MessageVisibility.valueOf(args[3]);

        if (!XenUtils.isValidEnum(MessageAction.class, args[4])) {
            Lang.send(sender, "commands.preview.invalid-action", "%action%;" + args[4]);
            return;
        }
        MessageAction action = MessageAction.valueOf(args[4]);

        String path = visibility.getConfigSection() + "." + action.getConfigSection() + "." + args[5];
        if (!messageType.getConfig().contains(path)) {
            Lang.send(sender, "commands.preview.invalid-message", "%id%;" + args[5]);
            return;
        }

        String randomKey = RandomHandler.getRandomKey(messageType.getConfig(), path + "." + messageType.getKey());
        messageType.handle(target, Collections.singletonList((Player) sender), path, randomKey, MessageVisibility.PRIVATE);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        switch (args.length) {
            case 2:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 3:
                return new ArrayList<>(MessageSender.getInstance().getMessageTypes().keySet());
            case 4:
                return Arrays.stream(MessageVisibility.values()).map(Enum::name).collect(Collectors.toList());
            case 5:
                return Arrays.stream(MessageAction.values()).map(Enum::name).collect(Collectors.toList());
        }
        return null;
    }

}
