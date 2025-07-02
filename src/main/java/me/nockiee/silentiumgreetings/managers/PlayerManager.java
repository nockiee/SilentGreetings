package me.nockiee.silentiumgreetings.managers;

import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class PlayerManager {
    public Player findTargetPlayer(Player player, double maxDistance) {
        RayTraceResult rayTrace = player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getEyeLocation().getDirection(),
            maxDistance,
            entity -> entity instanceof Player && !entity.equals(player)
        );
        if (rayTrace == null || !(rayTrace.getHitEntity() instanceof Player target)) {
            return null;
        }
        return target.getLocation().distance(player.getLocation()) <= maxDistance ? target : null;
    }
}