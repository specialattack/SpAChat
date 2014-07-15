
package net.specialattack.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class Util {

    private static Pattern colorPattern = Pattern.compile("&([0-9a-fA-F])");

    public static String colorize(String input) {
        Matcher matcher = colorPattern.matcher(input);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, ChatColor.values()[Integer.parseInt(matcher.group(1), 16)].toString());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
