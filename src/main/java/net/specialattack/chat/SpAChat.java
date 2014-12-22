package net.specialattack.chat;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.specialattack.bukkit.core.PluginState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SpAChat extends JavaPlugin {

    public static SpAChat instance;
    private static PluginState state = PluginState.Unloaded;
    private PluginDescriptionFile pdf;
    private Logger logger;

    public String format = "<&name> &msg";
    public String formatString;
    public List<Rank> ranks;
    public Map<String, String> worlds;

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

        this.loadFormatting();

        this.getServer().getPluginManager().registerEvents(new ChatEventHandler(), this);

        state = PluginState.Loaded;
    }

    @Override
    public void onDisable() {
        state = PluginState.Disabling;

        if (this.ranks != null) {
            for (Rank rank : this.ranks) {

                Bukkit.getPluginManager().removePermission("spachat." + rank.name);
            }
        }

        log(this.pdf.getFullName() + " is now disabled!");

        state = PluginState.Disabled;
    }

    private void loadFormatting() {
        FileConfiguration config = this.getConfig();

        this.ranks = new ArrayList<Rank>();
        ConfigurationSection ranks = config.getConfigurationSection("ranks");
        if (ranks != null) {
            Map<String, Object> ranksMap = ranks.getValues(false);

            for (Object obj : ranksMap.values()) {
                if (obj instanceof ConfigurationSection) {
                    Rank rank = new Rank((ConfigurationSection) obj);

                    this.ranks.add(rank);
                    Bukkit.getPluginManager().addPermission(new Permission("spachat." + rank.name, PermissionDefault.FALSE));
                }
            }
        }

        this.format = config.getString("format", format);
        this.formatString = Util.colorize(this.format);
        this.formatString = this.formatString.replaceAll("@name", "\\%1\\$s").replaceAll("@msg", "\\%2\\$s");
        this.formatString = this.formatString.replaceAll("@world", "\\%3\\$s").replaceAll("@health", "\\%4\\$s");

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

        this.saveConfig();
    }

    public static PluginState getState() {
        return state;
    }

    public void formatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = this.getFormattedName(player).replace("@name", "%1$s");
        String health = this.getHealthBar((int) player.getHealth(), (int) player.getMaxHealth());
        String world = this.getWorld(player);
        if (player.hasPermission("spachat.color")) {
            String message = event.getMessage();
            message = Util.colorize(message);
            event.setMessage(message);
        }

        String format = String.format(this.formatString, name, "%2$s", world, health);
        event.setFormat(format);
    }

    public String getFormattedName(Player player) {
        Set<Rank> ranks = new TreeSet<Rank>(new Comparator<Rank>() {
            @Override
            public int compare(Rank rank1, Rank rank2) {
                if (rank1.importance < rank2.importance) {
                    return 1;
                } else if (rank1.importance > rank2.importance) {
                    return -1;
                } else {
                    return rank1.name.compareTo(rank2.name);
                }
            }
        });
        for (Rank r : this.ranks) {
            if (player.hasPermission(r.getPermissionNode())) {
                ranks.add(r);
            }
        }

        String name = "@name";

        for (Rank r : ranks) {
            name = r.getFormattedName(name);
        }

        name = Util.colorize(name);
        return ChatColor.RESET + name + ChatColor.RESET;
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
