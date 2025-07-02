package me.nockiee.silentiumgreetings.commands;

import me.nockiee.silentiumgreetings.SilentiumGreetings;
import me.nockiee.silentiumgreetings.utils.MessageUtils;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HugCommand implements CommandExecutor {
    private final SilentiumGreetings plugin;

    public HugCommand(SilentiumGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cТолько для игроков!"));
            return true;
        }

        if (plugin.getCooldownManager().isOnCooldown(player, "hug")) {
            String msg = plugin.getConfig().getString("messages.errors.cooldown")
                .replace("{time}", String.valueOf(plugin.getCooldownManager().getRemainingTime(player, "hug")));
            player.sendMessage(MessageUtils.colorize(msg));
            return true;
        }

        Player target = plugin.getPlayerManager().findTargetPlayer(
            player, 
            plugin.getConfig().getDouble("settings.ranges.hug")
        );

        if (target == null) {
            String error = plugin.getConfig().getString("messages.errors.no-target")
                .replace("{distance}", String.valueOf(plugin.getConfig().getDouble("settings.ranges.hug")));
            player.sendMessage(MessageUtils.colorize(error));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.self-action")));
            return true;
        }

        String hugMsg = MessageUtils.formatMessage(
            plugin.getConfig().getStringList("messages.hugs"),
            player.getName(),
            target.getName()
        );

        MessageUtils.sendToNearby(
            player, 
            hugMsg, 
            plugin.getConfig().getDouble("settings.ranges.hug-message")
        );

        target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 1.5, 0), 5);
        plugin.getCooldownManager().setCooldown(
            player, 
            "hug", 
            plugin.getConfig().getInt("settings.timeouts.cooldown-hug")
        );

        return true;
    }
}