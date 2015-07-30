package net.specialattack.chat.provider;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface ValueProvider {

    String getDomain();

    String getName();

    BaseComponent[] getValue(Player player, String message);

    void parseFromConfig(ConfigurationSection section);

    String getDebugHierarchy(String prefix);
}
