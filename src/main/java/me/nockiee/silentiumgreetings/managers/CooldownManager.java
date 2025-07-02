package me.nockiee.silentiumgreetings.managers;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public boolean isOnCooldown(Player player, String action) {
        return getRemainingTime(player, action) > 0;
    }

    public long getRemainingTime(Player player, String action) {
        Map<UUID, Long> actionCooldowns = cooldowns.getOrDefault(action, new HashMap<>());
        Long expireTime = actionCooldowns.get(player.getUniqueId());
        return expireTime == null ? 0 : Math.max(0, (expireTime - System.currentTimeMillis()) / 1000);
    }

    public void setCooldown(Player player, String action, int seconds) {
        cooldowns.computeIfAbsent(action, k -> new HashMap<>())
                .put(player.getUniqueId(), System.currentTimeMillis() + seconds * 1000L);
    }
}