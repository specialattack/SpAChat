package net.specialattack.chat;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.specialattack.bukkit.core.PluginState;
import net.specialattack.chat.command.SpAChatCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SpAChat extends JavaPlugin {

    public static SpAChat instance;
    private static PluginState state = PluginState.Unloaded;
    private PluginDescriptionFile pdf;
    private Logger logger;

    protected Plugin craftIRC;

    public Format format;
    public Set<Tag> tags;
    public Map<String, String> worlds;

    public static boolean craftIRCFix;

    public SpAChat() {
        super();
        state = PluginState.Initializing;

        instance = this;

        state = PluginState.Initialized;
    }

    @Override
    public void onEnable() {
        state = PluginState.Loading;

        this.logger = this.getLogger();
        this.pdf = this.getDescription();

        this.craftIRC = Bukkit.getPluginManager().getPlugin("CraftIRC");

        this.getCommand("spachat").setExecutor(new SpAChatCommand());

        this.saveDefaultConfig();

        Format.loadDefaultProviders();

        this.loadConfig();

        this.getServer().getPluginManager().registerEvents(new ChatEventHandler(), this);

        state = PluginState.Loaded;
    }

    @Override
    public void onDisable() {
        state = PluginState.Disabling;

        this.unloadPermissions();

        log(this.pdf.getFullName() + " is now disabled!");

        state = PluginState.Disabled;
    }

    private void unloadPermissions() {
        if (this.tags != null) {
            for (Tag tag : this.tags) {
                Bukkit.getPluginManager().removePermission("spachat." + tag.name);
            }
        }
    }

    public void loadConfig() {
        this.unloadPermissions();
        this.reloadConfig();
        FileConfiguration config = this.getConfig();

        SpAChat.craftIRCFix = config.getBoolean("craftIRC-fix", false);

        try {
            this.format = Format.loadFormat(MapListSection.convert(config.getMapList("format")));
            SpAChat.log(Level.INFO, "Chat format is:" + this.format.getDebugHierarchy());
        } catch (Exception e) {
            SpAChat.log(Level.SEVERE, "Failed loading chat formatting", e);
        }

        this.tags = new TreeSet<Tag>(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                if (o1.sortingOrder < o2.sortingOrder) {
                    return -1;
                } else if (o1.sortingOrder > o2.sortingOrder) {
                    return 1;
                } else {
                    if (o1.priority < o2.priority) {
                        return -1;
                    } else if (o1.priority > o2.priority) {
                        return 1;
                    } else {
                        return o1.name.compareTo(o2.name);
                    }
                }
            }
        });
        ConfigurationSection ranks = config.getConfigurationSection("ranks");
        if (ranks != null) {
            Map<String, Object> ranksMap = ranks.getValues(false);

            for (Object obj : ranksMap.values()) {
                if (obj instanceof ConfigurationSection) {
                    Tag tag = Tag.loadTag((ConfigurationSection) obj);

                    this.tags.add(tag);
                    Bukkit.getPluginManager().addPermission(new Permission("spachat." + tag.name, PermissionDefault.FALSE));
                }
            }
        }

        this.worlds = new HashMap<String, String>();
        ConfigurationSection worlds = config.getConfigurationSection("worlds");
        if (worlds != null) {
            Map<String, Object> worldsMap = worlds.getValues(false);

            for (Entry<String, Object> entry : worldsMap.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof String) {
                    this.worlds.put(entry.getKey(), (String) obj);
                }
            }
        }
    }

    public static PluginState getState() {
        return state;
    }

    private String getWorld(Player player) {
        String worldname = player.getWorld().getName().toLowerCase();
        if (this.worlds.containsKey(worldname)) {
            worldname = this.worlds.get(worldname);
        }
        return worldname;
    }

    private String getHealthBar(int health, int maxHealth) {
        StringBuilder sb = new StringBuilder(ChatColor.RESET.toString());
        for (int i = 0; i < maxHealth; i += 2) {
            if (health > i) {
                sb.append(ChatColor.DARK_GREEN).append("|");
            } else {
                sb.append(ChatColor.DARK_RED).append("|");
            }
        }
        return sb.append(ChatColor.RESET).toString();
    }

    public static void log(String message) {
        instance.logger.log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        instance.logger.log(level, message);
    }

    public static void log(Level level, String message, Throwable throwable) {
        instance.logger.log(level, message, throwable);
    }

}
