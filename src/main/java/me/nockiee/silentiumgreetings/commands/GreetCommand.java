package me.nockiee.silentiumgreetings.commands;

import me.nockiee.silentiumgreetings.SilentiumGreetings;
import me.nockiee.silentiumgreetings.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GreetCommand implements CommandExecutor {
    private final SilentiumGreetings plugin;

    public GreetCommand(SilentiumGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cТолько для игроков!"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.usage")));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.player-offline")));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.self-action")));
            return true;
        }

        String message = MessageUtils.formatMessage(
            plugin.getConfig().getStringList("messages.greetings"),
            player.getName(),
            target.getName()
        );

        target.sendMessage(message);
        player.sendMessage(message);
        return true;
    }
}