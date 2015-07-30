package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class StringProvider extends BaseProvider {

    private String value;

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "string";
    }

    @Override
    public BaseComponent[] getValue(Player player, String message) {
        return new BaseComponent[] { this.applyStyles(new TextComponent(this.value)) };
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
        this.value = section.getString("value");
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "spachat:string '" + this.value + "'";
    }
}
