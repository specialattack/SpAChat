package net.specialattack.chat;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Tag {

    public final String name;
    private ChatColor nameColor;
    public int colorPriority;
    public int tagSortOrder;
    private Format tagFormat;

    private Tag(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public String getPermissionNode() {
        return Consts.PERM_RANK_START + this.name;
    }

    public static Tag loadTag(@Nonnull ConfigurationSection section) {
        Tag result = new Tag(section.getName());
        if (section.contains("name-color")) {
            result.nameColor = ChatColor.valueOf(section.getString("name-color"));
        }
        result.colorPriority = section.getInt("priority", 0);
        result.tagSortOrder = section.getInt("sorting-order", 0);
        result.tagFormat = Format.loadFormat(MapListSection.convert(section.getMapList("tag-format")));
        return result;
    }

    @Nonnull
    public static BaseComponent[] getTags(@Nonnull SpAChat plugin, @Nonnull Player player, @Nonnull String message) {
        return plugin.tagManager.tags.stream()
                .filter(tag -> tag.tagFormat != null && player.hasPermission(tag.getPermissionNode()))
                .map(tag -> tag.tagFormat.performFormat(plugin, player, message))
                .toArray(BaseComponent[]::new);
    }

    @Nullable
    public static ChatColor getPlayerColor(@Nonnull SpAChat plugin, @Nonnull Player player) {
        Optional<Tag> result = plugin.tagManager.tags.stream()
                .filter(tag -> tag.nameColor != null && player.hasPermission(tag.getPermissionNode()))
                .sorted((o1, o2) -> {
                    if (o1.colorPriority < o2.colorPriority) {
                        return -1;
                    } else if (o1.colorPriority > o2.colorPriority) {
                        return 1;
                    } else {
                        return o1.name.compareTo(o2.name);
                    }
                })
                .findFirst();
        return result.isPresent() ? result.get().nameColor : null;
    }
}
