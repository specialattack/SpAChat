package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MetaProvider extends BaseProvider {

    private String name;

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        // TODO: implement
        return new BaseComponent[] {};
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
        this.name = section.getString("name");
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "spachat:meta '" + this.name + "'";
    }
}
