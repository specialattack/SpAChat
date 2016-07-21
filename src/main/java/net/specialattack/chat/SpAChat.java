package net.specialattack.chat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.specialattack.chat.command.NickCommand;
import net.specialattack.chat.command.SpAChatCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SpAChat extends JavaPlugin {

    private PluginDescriptionFile pdf;
    private Logger log;

    public TagManager tagManager;

    public Map<UUID, String> nicknames;

    public boolean craftIRCFix;

    @Override
    public void onDisable() {
        this.unloadPermissions();

        this.log(this.pdf.getFullName() + " is now disabled!");
    }

    @Override
    public void onEnable() {
        this.log = this.getLogger();
        this.pdf = this.getDescription();

        this.getCommand("spachat").setExecutor(new SpAChatCommand(this));
        this.getCommand("nick").setExecutor(new NickCommand(this, Consts.PERM_COMMAND_NICK));

        this.saveDefaultConfig();

        Format.loadDefaultProviders();

        this.loadConfig();
        this.loadNicknames();

        this.getServer().getPluginManager().registerEvents(new Handler(this), this);

        this.log(this.pdf.getFullName() + " is now enabled!");
    }

    private void unloadPermissions() {
        if (this.tagManager != null) {
            this.tagManager.release();
            this.tagManager = null;
        }
    }

    public void loadConfig() {
        this.log("Loading configuration");

        this.unloadPermissions();
        this.reloadConfig();

        FileConfiguration config = this.getConfig();

        this.tagManager = new TagManager(this);
        this.tagManager.load(config);
    }

    public void loadNicknames() {
        File file = new File(this.getDataFolder(), "nicknames.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                this.log(Level.SEVERE, "Failed loading permissions file", e);
                return;
            }
        }

        this.nicknames = new HashMap<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getValues(false).entrySet().stream()
                .filter(entry -> entry.getValue() instanceof String)
                .forEach(entry -> this.nicknames.put(UUID.fromString(entry.getKey()), (String) entry.getValue()));
    }

    public void saveNicknames() {
        File file = new File(this.getDataFolder(), "nicknames.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                this.log(Level.SEVERE, "Failed loading permissions file", e);
                return;
            }
        }

        YamlConfiguration config = new YamlConfiguration();
        this.nicknames.forEach((uuid, name) -> config.set(uuid.toString(), name));
        try {
            config.save(file);
        } catch (IOException e) {
            this.log(Level.SEVERE, "Failed saving nicknames", e);
        }
    }

    public void updatePlayerNickname(Player player) {
        if (player.hasPermission(Consts.PERM_CUSTOM_NICK) && this.nicknames.containsKey(player.getUniqueId())) {
            String nickname = this.nicknames.get(player.getUniqueId());

            if (!player.hasPermission(Consts.PERM_CUSTOM_NICK_COLORS)) {
                nickname = ChatColor.stripColor(nickname);
            }
            player.setDisplayName(nickname);
            player.setPlayerListName(nickname);
            player.spigot();
        } else {
            player.setDisplayName(null);
            player.setPlayerListName(null);
        }
    }

    public String getWorld(Player player) {
        String worldname = player.getWorld().getName().toLowerCase();
        if (this.tagManager.worlds.containsKey(worldname)) {
            worldname = this.tagManager.worlds.get(worldname);
        }
        return worldname;
    }

    public String getHealthBar(Player player) {
        StringBuilder sb = new StringBuilder(ChatColor.RESET.toString());
        for (int i = 0; i < player.getMaxHealth(); i += 2) {
            if (player.getHealth() > i) {
                sb.append(ChatColor.DARK_GREEN).append("|");
            } else {
                sb.append(ChatColor.DARK_GRAY).append("|");
            }
        }
        return sb.append(ChatColor.RESET).toString();
    }

    public void log(String message) {
        this.log.log(Level.INFO, message);
    }

    public void log(Level level, String message) {
        this.log.log(level, message);
    }

    public void log(Level level, String message, Throwable throwable) {
        this.log.log(level, message, throwable);
    }
}
