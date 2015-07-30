package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.specialattack.chat.Tag;
import net.specialattack.chat.provider.ValueProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TagsProvider implements ValueProvider {

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "tags";
    }

    @Override
    public BaseComponent[] getValue(Player player, String message) {
        return Tag.getTags(player, message);
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "spachat:tags";
    }
}
