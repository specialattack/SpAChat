package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.Tag;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.entity.Player;

public class ColoredNameProvider extends BaseProvider {

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        BaseComponent component = this.applyStyles(new TextComponent(player.getDisplayName()));
        ChatColor color = Tag.getPlayerColor(plugin, player);
        if (color != null) {
            component.setColor(color); // override the color
        }
        return new BaseComponent[] { component };
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "spachat:name";
    }
}
