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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KissCommand implements CommandExecutor {
    private final SilentGreetings plugin;

    public KissCommand(SilentGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cOnly players can use this command!"));
            return true;
        }

        double maxDistance = plugin.getConfig().getDouble("settings.ranges.kiss", 20.0);
        Player target = plugin.getPlayerManager().findTargetPlayer(player, maxDistance);
        if (target == null) {
            String error = plugin.getConfig().getString("messages.errors.no-target")
                .replace("{distance}", String.valueOf((int) maxDistance));
            player.sendMessage(MessageUtils.colorize(error));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.errors.self-action")));
            return true;
        }

        double distance = player.getLocation().distance(target.getLocation());
        List<String> msgs;
        if (distance <= 1.0) {
            msgs = plugin.getConfig().getStringList("messages.kisses.close");
        } else {
            msgs = plugin.getConfig().getStringList("messages.kisses.air");
        }
        String msg = msgs.isEmpty() ? "" :
            msgs.get(ThreadLocalRandom.current().nextInt(msgs.size()))
                .replace("{player1}", player.getName())
                .replace("{player2}", target.getName());
        if (distance <= 1.0) {
            target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 1.5, 0), 8);
            target.playSound(target.getLocation(), Sound.ENTITY_CAT_PURR, 1.0f, 1.2f);
            MessageUtils.sendToNearby(player, MessageUtils.colorize(msg), plugin.getConfig().getDouble("settings.ranges.hug-message", 50.0));
        } else {
            target.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, target.getLocation().add(0, 1.5, 0), 5);
            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            MessageUtils.sendToNearby(player, MessageUtils.colorize(msg), plugin.getConfig().getDouble("settings.ranges.hug-message", 50.0));
        }

        return true;
    }
}