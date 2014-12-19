package net.specialattack.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static Pattern colorPattern = Pattern.compile("&([0-9a-fA-Fk-oK-OrR])");

    public static String colorize(String input) {
        Matcher matcher = colorPattern.matcher(input);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, '§' + matcher.group(1).toLowerCase());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
