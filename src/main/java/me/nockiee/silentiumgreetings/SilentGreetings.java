package me.nockiee.silentiumgreetings;

import me.nockiee.silentiumgreetings.commands.*;
import me.nockiee.silentiumgreetings.managers.*;
import me.nockiee.silentiumgreetings.data.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SilentGreetings extends JavaPlugin implements Listener { // <--- Переименовано
    private PlayerData playerData;
    private CooldownManager cooldownManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.playerData = new PlayerData(this);
        this.cooldownManager = new CooldownManager();
        this.playerManager = new PlayerManager();

        // Регистрация команд и алиасов из конфига
        registerCommandWithAliases("hi", new GreetCommand(this));
        registerCommandWithAliases("bye", new ByeCommand(this));
        registerCommandWithAliases("hug", new HugCommand(this));
        registerCommandWithAliases("five", new FiveCommand(this));
        registerCommandWithAliases("f", new FCommand(this));
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Плагин успешно активирован!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        playerData.setLastDeath(dead.getUniqueId(), System.currentTimeMillis() + ":" + dead.getName());
        playerData.setGlobalLastDeath(dead.getName(), System.currentTimeMillis());
    }

    private void registerCommandWithAliases(String name, CommandExecutor executor) {
        PluginCommand cmd = getCommand(name);
        if (cmd == null) return;
        cmd.setExecutor(executor);

        List<String> aliases = getConfig().getStringList("aliases." + name);
        if (!aliases.isEmpty()) {
            cmd.setAliases(aliases);
        }
    }

    public PlayerData getPlayerData() { return playerData; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public PlayerManager getPlayerManager() { return playerManager; }

    public String colorize(String message) {
        // HEX-цвета <#RRGGBB>
        Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String color = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + color).toString());
        }
        matcher.appendTail(buffer);
        // Стандартные коды цветов
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}