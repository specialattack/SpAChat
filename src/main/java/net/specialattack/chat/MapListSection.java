package net.specialattack.chat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

public class MapListSection extends MemorySection implements Configuration {

    public MapListSection(Map<?, ?> input) {
        this.convertMapsToSections(input, this);
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Object o : input.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                this.convertMapsToSections((Map) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    public static List<ConfigurationSection> convert(List<Map<?, ?>> input) {
        return input.stream()
                .map(MapListSection::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addDefaults(Map<String, Object> map) {
    }

    @Override
    public void addDefaults(Configuration configuration) {
    }

    @Override
    public void setDefaults(Configuration configuration) {
    }

    @Override
    public Configuration getDefaults() {
        return null;
    }

    @Override
    public ConfigurationOptions options() {
        return new ConfigurationOptions(this) {
        };
    }
}
