package net.insprill.cjm.commands;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.insprill.cjm.CJM;
import net.insprill.xenlib.commands.ICommandArgument;
import net.insprill.xenlib.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CjmArgPlInfo implements ICommandArgument {

    private static final String HASTEBIN_LINK = "https://paste.insprill.net/";

    @Override
    public String getBaseArg() {
        return "plinfo";
    }

    @Override
    public Map<String, Boolean> getSubArgs() {
        return ImmutableMap.of("skipPlugins", false);
    }

    @Override
    public String getDescription() {
        return "Create a hastebin with some information about your server.";
    }

    @Override
    public void process(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append("CJM: ").append(CJM.getInstance().getDescription().getVersion()).append("\n");
        builder.append("Server: ").append(Bukkit.getVersion()).append("\n");
        builder.append("API: ").append(Bukkit.getBukkitVersion()).append("\n");
        builder.append("JVM-Name: ").append(System.getProperty("java.vm.name")).append("\n");
        builder.append("JVM-Version: ").append(System.getProperty("java.vm.version")).append("\n");
        builder.append("Architecture: ").append(System.getProperty("os.arch")).append("\n");
        builder.append("OS-Name: ").append(System.getProperty("os.name")).append("\n");
        builder.append("OS-Version: ").append(System.getProperty("os.version")).append("\n");

        if (args.length != 2 || !args[1].equalsIgnoreCase("skipPlugins")) {
            builder.append("Enabled-Plugins:").append("\n");

            Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(Plugin::isEnabled)
                    .sorted(Comparator.comparing(Plugin::getName))
                    .forEach(pl -> {
                        builder.append("    ")
                                .append(pl.getName()).append(": ")
                                .append(pl.getDescription().getVersion()).append("\n");
                    });
        }

        try {
            URL url = new URL(HASTEBIN_LINK + "documents");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.getOutputStream().write(builder.toString().getBytes(Charsets.UTF_8));
            JsonObject object = new Gson().fromJson(new InputStreamReader(conn.getInputStream(), Charsets.UTF_8), JsonObject.class);
            String link = HASTEBIN_LINK + object.get("key").getAsString() + ".yml";
            Lang.send(sender, "commands.plinfo.success", "%link%;" + link);
        } catch (IOException exception) {
            Lang.send(sender, "commands.plinfo.fail", "%error%;" + exception.getMessage());
        }
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }

    @Override
    public String getPermission() {
        return "cjm.command.plinfo";
    }

}
