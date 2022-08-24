package net.insprill.cjm.commands;

import net.insprill.cjm.CustomJoinMessages;
import net.insprill.cjm.message.MessageSender;
import net.insprill.xenlib.commands.ICommandArgument;
import net.insprill.xenlib.files.YamlFile;
import net.insprill.xenlib.files.YamlFolder;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CjmArgReload implements ICommandArgument {

    @Override
    public String getBaseArg() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all configuration files.";
    }

    @Override
    public String getPermission() {
        return "cjm.command.reload";
    }

    @Override
    public void process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] strings) {
        YamlFolder.LOCALE.reload();
        YamlFile.CONFIG.reload();
//        WorldChangeEvent.getWorldLogConfig().reload(); //todo
        MessageSender messageSender = CustomJoinMessages.getPlugin(CustomJoinMessages.class).messageSender;
        messageSender.getTypeMap().values().forEach(m -> m.getConfig().reload());
        messageSender.setupPermissions();
        Lang.send(sender, "commands.reload");
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] strings) {
        return null;
    }

}
