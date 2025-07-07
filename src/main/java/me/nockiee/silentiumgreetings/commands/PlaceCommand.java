package me.nockiee.silentiumgreetings.commands;

import me.nockiee.silentiumgreetings.SilentGreetings;
import me.nockiee.silentiumgreetings.utils.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlaceCommand implements CommandExecutor {
    private final SilentGreetings plugin;
    private final Map<UUID, Location> lastReceivedLocation = new HashMap<>();
    private final Map<UUID, BossBar> pinnedBars = new HashMap<>();
    private final Map<UUID, Double> initialDistances = new HashMap<>(); // <--- добавлено
    private final Map<UUID, UUID> sharedBy = new HashMap<>(); // <--- кто поделился координатами

    public PlaceCommand(SilentGreetings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.place.usage")));
            return true;
        }

        if (args[0].equalsIgnoreCase("pin")) {
            Location loc = lastReceivedLocation.get(player.getUniqueId());
            if (loc == null) {
                player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.place.no_location")));
                return true;
            }
            BossBar bar = pinnedBars.get(player.getUniqueId());
            if (bar == null) {
                bar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
                pinnedBars.put(player.getUniqueId(), bar);
            }
            bar.addPlayer(player);

            // Сохраняем начальную дистанцию
            double initialDist = player.getLocation().distance(loc);
            initialDistances.put(player.getUniqueId(), initialDist);

            updateBossBar(player, loc, bar, initialDist);
            player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.place.pinned")));
            return true;
        }

        if (args[0].equalsIgnoreCase("unpin")) {
            BossBar bar = pinnedBars.remove(player.getUniqueId());
            initialDistances.remove(player.getUniqueId());
            if (bar != null) {
                bar.removeAll();
                player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.place.unpinned")));
            } else {
                player.sendMessage(MessageUtils.colorize(plugin.getConfig().getString("messages.place.no_pinned")));
            }
            return true;
        }

        // /place <player>
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(MessageUtils.colorize("&cPlayer not found or offline!"));
            return true;
        }
        Location loc = player.getLocation();
        String msg = plugin.getConfig().getString("messages.place.received")
                .replace("{player}", player.getName())
                .replace("{x}", String.valueOf(loc.getBlockX()))
                .replace("{y}", String.valueOf(loc.getBlockY()))
                .replace("{z}", String.valueOf(loc.getBlockZ()));

        net.md_5.bungee.api.chat.BaseComponent[] components = TextComponent.fromLegacyText(MessageUtils.colorize(msg));
        if (components.length > 0) {
            // Найти компонент с координатами (обычно последний)
            TextComponent coordsComponent = null;
            for (int i = components.length - 1; i >= 0; i--) {
                String txt = components[i].toPlainText();
                if (txt.matches(".*\\[\\d+, \\d+, \\d+\\].*")) {
                    coordsComponent = (TextComponent) components[i];
                    break;
                }
            }
            if (coordsComponent == null && components[components.length - 1] instanceof TextComponent) {
                coordsComponent = (TextComponent) components[components.length - 1];
            }
            if (coordsComponent != null) {
                coordsComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/place pin"));
                coordsComponent.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                        net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Закрепить эту точку (/place pin)").create()
                ));
            }
        }
        target.spigot().sendMessage(components);
        lastReceivedLocation.put(target.getUniqueId(), loc);
        player.sendMessage(MessageUtils.colorize(
            plugin.getConfig().getString("messages.place.sent")
                .replace("{player}", target.getName())
        ));
        // Сохраняем, кто поделился координатами
        sharedBy.put(target.getUniqueId(), player.getUniqueId());
        return true;
    }

    // Обновление bossbar (вызывайте периодически через scheduler)
    public void updateBossBar(Player player, Location pinnedLoc, BossBar bar, double initialDist) {
        Location current = player.getLocation();
        double currentDist = current.distance(pinnedLoc);

        // Прогресс: 0 на старте, 1 при достижении точки
        double progress = Math.max(0.0, Math.min(1.0, (initialDist - currentDist) / initialDist));

        // Стрелка направления
        String arrow = getDirectionArrow(current, pinnedLoc);

        String title = MessageUtils.colorize("&bТочка: <#ffe066>[{x}, {y}, {z}] &7{arrow} (&f{dist} блоков&7)")
                .replace("{x}", String.valueOf(pinnedLoc.getBlockX()))
                .replace("{y}", String.valueOf(pinnedLoc.getBlockY()))
                .replace("{z}", String.valueOf(pinnedLoc.getBlockZ()))
                .replace("{dist}", String.valueOf((int) currentDist))
                .replace("{arrow}", arrow);

        bar.setTitle(title);
        bar.setProgress(progress);

        // Если подошёл ближе 10 блоков — убрать bar и уведомить
        if (currentDist < 10.0) {
            bar.removeAll();
            pinnedBars.remove(player.getUniqueId());
            initialDistances.remove(player.getUniqueId());
            List<String> reachedMsgs = plugin.getConfig().getStringList("messages.place.reached");
            String reachedMsg = reachedMsgs.isEmpty() ? "&aВы достигли точки назначения!" :
                reachedMsgs.get(ThreadLocalRandom.current().nextInt(reachedMsgs.size()));
            player.sendMessage(MessageUtils.colorize(reachedMsg));
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

            // Сообщение и звук отправителю координат
            UUID senderId = sharedBy.get(player.getUniqueId());
            if (senderId != null) {
                Player sender = Bukkit.getPlayer(senderId);
                if (sender != null && sender.isOnline()) {
                    sender.sendMessage(MessageUtils.colorize("&a" + player.getName() + " достиг вашей точки!"));
                    sender.playSound(sender.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
                sharedBy.remove(player.getUniqueId());
            }
        }
    }

    // Для доступа из главного плагина
    public void tickAllBars() {
        for (Map.Entry<UUID, BossBar> entry : pinnedBars.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            Location loc = lastReceivedLocation.get(entry.getKey());
            Double initialDist = initialDistances.get(entry.getKey());
            if (player != null && loc != null && initialDist != null) {
                updateBossBar(player, loc, entry.getValue(), initialDist);
            }
        }
    }

    // Получить стрелку направления
    private String getDirectionArrow(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angleToTarget = Math.toDegrees(Math.atan2(-dx, dz));
        if (angleToTarget < 0) angleToTarget += 360;

        float playerYaw = from.getYaw();
        if (playerYaw < 0) playerYaw += 360;

        double relative = angleToTarget - playerYaw;
        if (relative < 0) relative += 360;

        if (relative >= 315 || relative < 45) return "↑";
        if (relative >= 45 && relative < 135) return "→";
        if (relative >= 135 && relative < 225) return "↓";
        if (relative >= 225 && relative < 315) return "←";
        return "●";
    }
}