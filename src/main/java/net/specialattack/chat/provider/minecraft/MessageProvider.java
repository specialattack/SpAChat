package net.specialattack.chat.provider.minecraft;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MessageProvider extends BaseProvider {

    @Override
    public String getDomain() {
        return "minecraft";
    }

    @Override
    public String getName() {
        return "message";
    }

    @Override
    public BaseComponent[] getValue(Player player, String message) {
        return new BaseComponent[] { this.applyStyles(new TextComponent(message)) };
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "minecraft:message";
    }
}