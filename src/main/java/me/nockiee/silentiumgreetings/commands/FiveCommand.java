package me.nockiee.silentiumgreetings.commands;

import me.nockiee.silentiumgreetings.SilentGreetings;
import me.nockiee.silentiumgreetings.utils.MessageUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FiveCommand implements CommandExecutor {
    private final SilentGreetings plugin;

    public FiveCommand(SilentGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cТолько для игроков!"));
            return true;
        }

        // Кулдаун для five
        if (plugin.getCooldownManager().isOnCooldown(player, "five")) {
            String msg = plugin.getConfig().getString("messages.errors.cooldown")
                .replace("{time}", String.valueOf(plugin.getCooldownManager().getRemainingTime(player, "five")));
            player.sendMessage(MessageUtils.colorize(msg));
            return true;
        }

        Player target = plugin.getPlayerManager().findTargetPlayer(player, 5.0);
        if (target == null) {
            String error = plugin.getConfig().getString("messages.errors.no-target")
                .replace("{distance}", "5");
            player.sendMessage(MessageUtils.colorize(error));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.self-action")));
            return true;
        }

        String fiveMsg = MessageUtils.formatMessage(
            plugin.getConfig().getStringList("messages.fives"),
            player.getName(),
            target.getName()
        );

        MessageUtils.sendToNearby(
            player,
            fiveMsg,
            plugin.getConfig().getDouble("settings.ranges.five-message")
        );
        target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 1.5, 0), 8);
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);

        // Установить кулдаун
        plugin.getCooldownManager().setCooldown(
            player,
            "five",
            plugin.getConfig().getInt("settings.timeouts.cooldown-five", 10) // по умолчанию 10 сек
        );

        return true;
    }
}