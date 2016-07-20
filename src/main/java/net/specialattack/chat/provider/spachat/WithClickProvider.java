package net.specialattack.chat.provider.spachat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.specialattack.chat.Format;
import net.specialattack.chat.MapListSection;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import net.specialattack.chat.provider.ValueProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class WithClickProvider extends BaseProvider {

    private Format format;
    private ClickEvent.Action action;
    private Format valueFormat;

    @Override
    public String getDomain() {
        return "spachat";
    }

    @Override
    public String getName() {
        return "withClick";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        BaseComponent result = this.format.performFormat(plugin, player, message);
        result.setClickEvent(new ClickEvent(this.action, this.valueFormat.performFormat(plugin, player, message).toPlainText()));
        return new BaseComponent[] { this.applyStyles(result) };
    }

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        super.parseFromConfig(section);
        this.format = Format.loadFormat(MapListSection.convert(section.getMapList("format")));
        String action = section.getString("action");
        if (action == null) {
            throw new IllegalArgumentException("withClick action must be either OPEN_URL, OPEN_FILE, RUN_COMMAND, or SUGGEST_COMMAND");
        }
        this.action = ClickEvent.Action.valueOf(action);
        this.valueFormat = Format.loadFormat(MapListSection.convert(section.getMapList("value-format")));
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        String result = prefix + "spachat:withClick action=" + this.action.name();
        result += "\n" + prefix + "  Value Formatting:";
        for (ValueProvider provider : this.valueFormat.getProviders()) {
            result += "\n" + provider.getDebugHierarchy(prefix + "    ");
        }
        result += "\n" + prefix + "  Inside Formatting:";
        for (ValueProvider provider : this.format.getProviders()) {
            result += "\n" + provider.getDebugHierarchy(prefix + "    ");
        }
        return result;
    }
}
