package net.specialattack.chat.provider.spachat;

import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.specialattack.chat.Format;
import net.specialattack.chat.MapListSection;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import net.specialattack.chat.provider.ValueProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class WithHoverProvider extends BaseProvider {

    private Format format;
    private HoverEvent.Action action;
    private Format[] valueFormat;

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "withHover";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        BaseComponent result = this.format.performFormat(plugin, player, message);
        BaseComponent[] eventValue = new BaseComponent[this.valueFormat.length];
        for (int i = 0; i < this.valueFormat.length; i++) {
            eventValue[i] = this.valueFormat[i].performFormat(plugin, player, message);
            if (i < this.valueFormat.length - 1) {
                eventValue[i].addExtra("\n");
            }
        }
        result.setHoverEvent(new HoverEvent(this.action, eventValue));
        return new BaseComponent[] { this.applyStyles(result) };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
        this.format = Format.loadFormat(MapListSection.convert(section.getMapList("format")));
        String action = section.getString("action");
        if (action == null) {
            throw new IllegalArgumentException("withHover action must be either SHOW_TEXT, SHOW_ACHIEVEMENT, or SHOW_ITEM");
        }
        this.action = HoverEvent.Action.valueOf(action);
        List<?> list = section.getList("value-format");
        if (list.size() <= 0) {
            throw new IllegalArgumentException("value-format needs to have at least one thing in it");
        }
        Object obj = list.get(0);
        if (obj instanceof List) {
            this.valueFormat = new Format[list.size()];
            for (int i = 0; i < list.size(); i++) {
                this.valueFormat[i] = Format.loadFormat(MapListSection.convert((List<Map<?, ?>>) list.get(i)));
            }
        } else if (obj instanceof Map) {
            this.valueFormat = new Format[] { Format.loadFormat(MapListSection.convert((List<Map<?, ?>>) list)) };
        } else {
            throw new IllegalArgumentException("Bad input format for value-format");
        }
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        String result = prefix + "spachat:withHover action=" + this.action.name();
        result += "\n" + prefix + "  Value Formatting: " + this.valueFormat.length;
        for (int i = 0; i < this.valueFormat.length; i++) {
            Format format = this.valueFormat[i];
            result += "\n" + prefix + "   " + i + ":";
            for (ValueProvider provider : format.getProviders()) {
                result += "\n" + provider.getDebugHierarchy(prefix + "    ");
            }
        }
        result += "\n" + prefix + "  Inside Formatting:";
        for (ValueProvider provider : this.format.getProviders()) {
            result += "\n" + provider.getDebugHierarchy(prefix + "    ");
        }
        return result;
    }
}
