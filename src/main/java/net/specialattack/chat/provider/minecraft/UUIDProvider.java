package net.specialattack.chat.provider.minecraft;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class UUIDProvider extends BaseProvider {

    @Override
    public String getDomain() {
        return "minecraft";
    }

    @Override
    public String getName() {
        return "uuid";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        return new BaseComponent[] { this.applyStyles(new TextComponent(player.getUniqueId().toString())) };
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "minecraft:uuid";
    }
}
