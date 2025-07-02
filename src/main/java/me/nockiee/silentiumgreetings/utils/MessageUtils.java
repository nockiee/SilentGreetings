package me.nockiee.silentiumgreetings.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MessageUtils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendToNearby(Player center, String message, double radius) {
        center.getNearbyEntities(radius, radius, radius).stream()
            .filter(e -> e instanceof Player)
            .forEach(p -> ((Player)p).sendMessage(message));
        center.sendMessage(message);
    }

    public static String formatMessage(List<String> messages, String... replacements) {
        String msg = messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
        msg = msg.replace("{player1}", replacements[0])
                .replace("{player2}", replacements[1])
                .replace("{player}", replacements[0])
                .replace("{dead_player}", replacements[1]);
        return colorize(msg);
    }
}