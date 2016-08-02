package net.specialattack.chat.provider.minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.specialattack.chat.Consts;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.provider.BaseProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageProvider extends BaseProvider {

    private static Pattern colorPattern = Pattern.compile("&([0-9a-fA-Fk-oK-OrR])");
    // Credit: https://mathiasbynens.be/demo/url-regex
    private static final Pattern urlPattern = Pattern.compile("(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?", Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);

    @Override
    public String getDomain() {
        return "minecraft";
    }

    @Override
    public String getName() {
        return "message";
    }

    @Override
    public BaseComponent[] getValue(SpAChat plugin, Player player, String message) {
        List<TextComponent> components = new ArrayList<>();
        boolean canColor = player.hasPermission(Consts.PERM_USE_COLORS);
        long[] urlLocations = findURLS(message);
        Map<Integer, ChatColor> codeLocations = findCodes(message, urlLocations);

        TextComponent component = new TextComponent();
        int prevLocation = 0;
        main:
        for (int i = 0; i < message.length(); i++) {
            if (canColor && codeLocations.containsKey(i)) {
                ChatColor code = codeLocations.get(i);

                if (prevLocation < i) {
                    TextComponent prev = component;
                    component = new TextComponent(component);
                    prev.setText(message.substring(prevLocation, i));
                    if (!prev.getText().isEmpty()) {
                        components.add(prev);
                    }
                }

                prevLocation = i + 2;

                switch (code) {
                    case MAGIC:
                        component.setObfuscated(true);
                        break;
                    case BOLD:
                        component.setBold(true);
                        break;
                    case STRIKETHROUGH:
                        component.setStrikethrough(true);
                        break;
                    case UNDERLINE:
                        component.setUnderlined(true);
                        break;
                    case ITALIC:
                        component.setItalic(true);
                        break;
                    case RESET:
                        component = new TextComponent();
                        break;
                    default:
                        component = new TextComponent();
                        component.setColor(code.asBungee());
                        break;
                }
            } else {
                for (long url : urlLocations) {
                    if (i >= (int) url && i < (int) (url >> 32)) {
                        if (prevLocation < i) {
                            TextComponent prev = component;
                            component = new TextComponent(component);
                            prev.setText(message.substring(prevLocation, i));
                            components.add(prev);
                        }

                        i = (int) (url >> 32) - 1;
                        TextComponent prev = component;
                        String target = message.substring((int) url, (int) (url >> 32));
                        prev.setText(target);
                        prev.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, target));
                        component = new TextComponent();
                        prevLocation = i + 1;
                        components.add(prev);
                        continue main;
                    }
                }
            }
        }

        if (prevLocation < message.length()) {
            component.setText(message.substring(prevLocation));
            components.add(component);
        }

        BaseComponent result = new TextComponent("");
        components.forEach(result::addExtra);
        return new BaseComponent[] { this.applyStyles(result) };
    }

    @Override
    public String getDebugHierarchy(String prefix) {
        return prefix + "minecraft:message";
    }

    @Nonnull
    private static Map<Integer, ChatColor> findCodes(@Nonnull String input, @Nonnull long[] urlLocations) {
        TreeMap<Integer, ChatColor> result = new TreeMap<>();
        Matcher matcher = colorPattern.matcher(input);
        match:
        while (matcher.find()) {
            int position = matcher.start();
            for (long url : urlLocations) {
                if (position >= (int) url && position < (int) (url >> 32)) {
                    continue match;
                }
            }

            result.put(position, ChatColor.getByChar(matcher.group().charAt(1)));
        }
        return result;
    }

    @Nonnull
    private static long[] findURLS(@Nonnull String input) {
        Matcher matcher = urlPattern.matcher(input);

        List<Long> result = new ArrayList<>();
        while (matcher.find()) {
            result.add((long) matcher.start() | ((long) matcher.end() << 32));
        }

        return result.stream().mapToLong(Long::longValue).toArray();
    }
}
