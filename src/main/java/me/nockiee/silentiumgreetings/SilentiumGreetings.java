package me.nockiee.silentiumgreetings;

import me.nockiee.silentiumgreetings.commands.*;
import me.nockiee.silentiumgreetings.managers.*;
import me.nockiee.silentiumgreetings.data.PlayerData;  // <-- Add this line
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player; // <--- добавьте этот импорт

public class SilentiumGreetings extends JavaPlugin implements Listener {
    private PlayerData playerData;
    private CooldownManager cooldownManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.playerData = new PlayerData(this);
        this.cooldownManager = new CooldownManager();
        this.playerManager = new PlayerManager();

        getCommand("hi").setExecutor(new GreetCommand(this));
        getCommand("hug").setExecutor(new HugCommand(this));
        getCommand("f").setExecutor(new FCommand(this));
        getCommand("bye").setExecutor(new ByeCommand(this));    // <-- добавить
        getCommand("five").setExecutor(new FiveCommand(this));  // <-- добавить
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Плагин успешно активирован!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        playerData.setLastDeath(dead.getUniqueId(), System.currentTimeMillis() + ":" + dead.getName());
        playerData.setGlobalLastDeath(dead.getName(), System.currentTimeMillis());
    }

    public PlayerData getPlayerData() { return playerData; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public PlayerManager getPlayerManager() { return playerManager; }
}