package net.specialattack.chat.provider;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;

public abstract class BaseProvider implements ValueProvider {

    public Boolean bold, italic, underlined, strikethrough, obfuscated;
    public ChatColor color;

    @Override
    public void parseFromConfig(ConfigurationSection section) {
        if (section.contains("bold")) {
            this.bold = section.getBoolean("bold");
        }
        if (section.contains("underlined")) {
            this.underlined = section.getBoolean("underlined");
        }
        if (section.contains("strikethrough")) {
            this.strikethrough = section.getBoolean("strikethrough");
        }
        if (section.contains("obfuscated")) {
            this.obfuscated = section.getBoolean("obfuscated");
        }
        if (section.contains("color")) {
            this.color = ChatColor.valueOf(section.getString("color"));
        }
    }

    public BaseComponent applyStyles(BaseComponent component) {
        if (this.bold != null) {
            component.setBold(this.bold);
        }
        if (this.underlined != null) {
            component.setUnderlined(this.underlined);
        }
        if (this.strikethrough != null) {
            component.setStrikethrough(this.strikethrough);
        }
        if (this.obfuscated != null) {
            component.setObfuscated(this.obfuscated);
        }
        if (this.color != null) {
            component.setColor(this.color);
        }
        return component;
    }
}
