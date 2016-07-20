package net.specialattack.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class TagManager {

    private final SpAChat plugin;

    public Format format;
    public Set<Tag> tags;
    public Map<String, String> worlds;

    public TagManager(@Nonnull SpAChat plugin) {
        this.plugin = plugin;
    }

    public void load(@Nonnull ConfigurationSection section) {
        this.plugin.craftIRCFix = section.getBoolean("craftIRC-fix", false);

        try {
            this.format = Format.loadFormat(MapListSection.convert(section.getMapList("format")));
            this.plugin.log(Level.INFO, "Chat format is:" + this.format.getDebugHierarchy());
        } catch (Exception e) {
            this.plugin.log(Level.SEVERE, "Failed loading chat formatting", e);
        }

        this.tags = new TreeSet<>((o1, o2) -> {
            if (o1.tagSortOrder < o2.tagSortOrder) {
                return -1;
            } else if (o1.tagSortOrder > o2.tagSortOrder) {
                return 1;
            } else {
                if (o1.colorPriority < o2.colorPriority) {
                    return -1;
                } else if (o1.colorPriority > o2.colorPriority) {
                    return 1;
                } else {
                    return o1.name.compareTo(o2.name);
                }
            }
        });
        ConfigurationSection ranks = section.getConfigurationSection("ranks");
        if (ranks != null) {
            Map<String, Object> ranksMap = ranks.getValues(false);

            ranksMap.values().stream()
                    .filter(obj -> obj instanceof ConfigurationSection)
                    .map(obj -> (ConfigurationSection) obj)
                    .forEach(sec -> {
                        Tag tag = Tag.loadTag(sec);

                        this.tags.add(tag);
                        Bukkit.getPluginManager().addPermission(new Permission(tag.getPermissionNode(), PermissionDefault.FALSE));
                    });
        }

        this.worlds = new HashMap<>();
        ConfigurationSection worlds = section.getConfigurationSection("worlds");
        if (worlds != null) {
            Map<String, Object> worldsMap = worlds.getValues(false);

            for (Map.Entry<String, Object> entry : worldsMap.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof String) {
                    this.worlds.put(entry.getKey(), (String) obj);
                }
            }
        }
    }

    public void release() {
        this.format = null;
        if (this.tags != null) {
            for (Tag tag : this.tags) {
                this.plugin.getServer().getPluginManager().removePermission(tag.getPermissionNode());
            }
            this.tags = null;
        }
        this.worlds = null;
    }
}
