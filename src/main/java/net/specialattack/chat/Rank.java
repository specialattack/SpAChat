
package net.specialattack.chat;

import org.bukkit.configuration.ConfigurationSection;

public class Rank {

    public String name;
    public String prefix;
    public String suffix;
    public int importance;

    public Rank(ConfigurationSection section) {
        this.name = section.getName();
        this.prefix = section.getString("prefix", "");
        this.suffix = section.getString("suffix", "");
        this.importance = section.getInt("importance", 1);
    }

    public String getPermissionNode() {
        return "spachat." + this.name;
    }

    public String getFormattedName(String name) {
        return this.prefix + name + this.suffix;
    }

}
