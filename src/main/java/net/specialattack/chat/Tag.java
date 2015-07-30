package net.specialattack.chat;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Tag {

    public String name;
    private ChatColor nameColor;
    public int priority;
    public int sortingOrder;
    private Format tagFormat;

    private Tag(String name) {
        this.name = name;
    }

    public String getPermissionNode() {
        return "spachat." + this.name;
    }

    public static Tag loadTag(ConfigurationSection section) {
        Tag result = new Tag(section.getName());
        if (section.contains("name-color")) {
            result.nameColor = ChatColor.valueOf(section.getString("name-color"));
        }
        result.priority = section.getInt("priority", 0);
        result.sortingOrder = section.getInt("sorting-order", 0);
        result.tagFormat = Format.loadFormat(MapListSection.convert(section.getMapList("tag-format")));
        return result;
    }

    public static BaseComponent[] getTags(Player player, String message) {
        List<BaseComponent> result = new ArrayList<BaseComponent>();
        for (Tag tag : SpAChat.instance.tags) {
            if (tag.tagFormat != null && player.hasPermission(tag.getPermissionNode())) {
                result.add(tag.tagFormat.performFormat(player, message));
            }
        }
        return result.toArray(new BaseComponent[result.size()]);
    }

    public static ChatColor getPlayerColor(Player player) {
        for (Tag tag : SpAChat.instance.tags) {
            if (tag.nameColor != null && player.hasPermission(tag.getPermissionNode())) {
                return tag.nameColor;
            }
        }
        return null;
    }

    private static class Requirement {

        private List<String> metas;

        private Requirement(List<String> metas) {
            this.metas = metas;
        }
    }

}
