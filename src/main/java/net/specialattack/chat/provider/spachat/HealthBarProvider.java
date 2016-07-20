package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HealthBarProvider extends BaseProvider {

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "healthbar";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        return new BaseComponent[] { this.applyStyles(new TextComponent(plugin.getHealthBar(player))) };
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "spachat:healthbar";
    }
}
