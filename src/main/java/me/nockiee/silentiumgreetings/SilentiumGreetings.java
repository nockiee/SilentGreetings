package me.nockiee.silentiumgreetings;

import me.nockiee.silentiumgreetings.commands.*;
import me.nockiee.silentiumgreetings.managers.*;
import me.nockiee.silentiumgreetings.data.PlayerData;  // <-- Add this line
import org.bukkit.plugin.java.JavaPlugin;

public class SilentiumGreetings extends JavaPlugin {
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

        getLogger().info("Плагин успешно активирован!");
    }

    public PlayerData getPlayerData() { return playerData; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public PlayerManager getPlayerManager() { return playerManager; }
}