package me.nockiee.silentiumgreetings.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");

    public static String colorize(String message) {
        if (message == null) return "";
        message = applyHexColors(message);
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String applyHexColors(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append('§').append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
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