package me.nockiee.silentiumgreetings.commands;

import me.nockiee.silentiumgreetings.SilentiumGreetings;
import me.nockiee.silentiumgreetings.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FCommand implements CommandExecutor {
    private final SilentiumGreetings plugin;

    public FCommand(SilentiumGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cТолько для игроков!"));
            return true;
        }

        String lastDeath = plugin.getPlayerData().getGlobalLastDeath();
        if (lastDeath == null || System.currentTimeMillis() - Long.parseLong(lastDeath.split(":")[0]) >
            plugin.getConfig().getInt("settings.timeouts.respect") * 1000L) {
            sender.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.no-recent-death")));
            return true;
        }

        String deadPlayer = lastDeath.split(":")[1];
        String respectMsg = MessageUtils.formatMessage(
            plugin.getConfig().getStringList("messages.respects"),
            player.getName(),
            deadPlayer
        );

        Bukkit.broadcastMessage(respectMsg);
        return true;
    }
}