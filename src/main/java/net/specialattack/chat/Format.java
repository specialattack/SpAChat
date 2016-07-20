package net.specialattack.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.provider.ValueProvider;
import net.specialattack.chat.provider.minecraft.MessageProvider;
import net.specialattack.chat.provider.minecraft.NameProvider;
import net.specialattack.chat.provider.minecraft.RealNameProvider;
import net.specialattack.chat.provider.minecraft.UUIDProvider;
import net.specialattack.chat.provider.spachat.ColoredNameProvider;
import net.specialattack.chat.provider.spachat.HealthBarProvider;
import net.specialattack.chat.provider.spachat.MetaProvider;
import net.specialattack.chat.provider.spachat.StringProvider;
import net.specialattack.chat.provider.spachat.TagsProvider;
import net.specialattack.chat.provider.spachat.WithClickProvider;
import net.specialattack.chat.provider.spachat.WithHoverProvider;
import net.specialattack.chat.provider.spachat.WorldProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Format {

    private static Map<String, Class<? extends ValueProvider>> valueProviders = new HashMap<>();
    private ValueProvider[] providers;

    private Format() {
    }

    public BaseComponent performFormat(SpAChat plugin, Player player, String message) {
        BaseComponent base = new TextComponent("");
        for (ValueProvider provider : this.providers) {
            for (BaseComponent component : provider.getValue(plugin, player, message)) {
                base.addExtra(component);
            }
        }
        return base;
    }

    public String getDebugHierarchy() {
        String result = "";
        for (ValueProvider provider : this.providers) {
            result += "\n" + provider.getDebugHierarchy("  ");
        }
        return result;
    }

    public ValueProvider[] getProviders() {
        return this.providers;
    }

    public static Format loadFormat(List<ConfigurationSection> options) {
        Format result = new Format();
        result.providers = new ValueProvider[options.size()];
        for (int i = options.size() - 1; i >= 0; i--) {
            ConfigurationSection section = options.get(i);
            String name = section.getString("type");
            Class<? extends ValueProvider> clazz = Format.valueProviders.get(name);
            if (clazz == null) {
                throw new IllegalArgumentException("Unknown provider type: " + name);
            }
            ValueProvider provider = Format.createInstance(clazz);
            provider.parseFromConfig(section);
            result.providers[i] = provider;
        }
        return result;
    }

    public static void registerProvider(Class<? extends ValueProvider> clazz) {
        ValueProvider instance = Format.createInstance(clazz);
        Format.valueProviders.put(instance.getDomain() + ":" + instance.getName(), clazz);
    }

    private static ValueProvider createInstance(Class<? extends ValueProvider> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("ValueProvider must have an empty constructor", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("ValueProvider must have an accessible empty constructor", e);
        }
    }

    protected static void loadDefaultProviders() {
        // Minecraft namespace
        Format.registerProvider(NameProvider.class);
        Format.registerProvider(RealNameProvider.class);
        Format.registerProvider(MessageProvider.class);
        Format.registerProvider(UUIDProvider.class);
        // SpAChat namespace
        Format.registerProvider(ColoredNameProvider.class);
        Format.registerProvider(StringProvider.class);
        Format.registerProvider(TagsProvider.class);
        Format.registerProvider(MetaProvider.class);
        Format.registerProvider(WithClickProvider.class);
        Format.registerProvider(WithHoverProvider.class);
        Format.registerProvider(HealthBarProvider.class);
        Format.registerProvider(WorldProvider.class);
    }
}
