package me.nockiee.silentiumgreetings.data;

import me.nockiee.silentiumgreetings.SilentGreetings;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {
    private final YamlConfiguration data;
    private final File file;

    public PlayerData(SilentGreetings plugin) {
        this.file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) plugin.saveResource("data.yml", false);
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public String getLastDeath(UUID uuid) {
        return data.getString(uuid + ".last-death");
    }

    public void setLastDeath(UUID uuid, String value) {
        data.set(uuid + ".last-death", value);
        save();
    }

    public void setGlobalLastDeath(String playerName, long timestamp) {
        data.set("global-last-death", timestamp + ":" + playerName);
        save();
    }

    public String getGlobalLastDeath() {
        return data.getString("global-last-death");
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}