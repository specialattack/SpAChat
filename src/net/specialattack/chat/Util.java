package net.specialattack.chat;

import org.bukkit.ChatColor;

public class Util {

	public static String colorize(String announce) {
		announce = announce.replaceAll("&AQUA;", ChatColor.AQUA.toString());
		announce = announce.replaceAll("&BLACK;", ChatColor.BLACK.toString());
		announce = announce.replaceAll("&BLUE;", ChatColor.BLUE.toString());
		announce = announce.replaceAll("&DARK_AQUA;", ChatColor.DARK_AQUA.toString());
		announce = announce.replaceAll("&DARK_BLUE;", ChatColor.DARK_BLUE.toString());
		announce = announce.replaceAll("&DARK_GRAY;", ChatColor.DARK_GRAY.toString());
		announce = announce.replaceAll("&DARK_GREEN;", ChatColor.DARK_GREEN.toString());
		announce = announce.replaceAll("&DARK_PURPLE;", ChatColor.DARK_PURPLE.toString());
		announce = announce.replaceAll("&DARK_RED;", ChatColor.DARK_RED.toString());
		announce = announce.replaceAll("&GOLD;", ChatColor.GOLD.toString());
		announce = announce.replaceAll("&GRAY;", ChatColor.GRAY.toString());
		announce = announce.replaceAll("&GREEN;", ChatColor.GREEN.toString());
		announce = announce.replaceAll("&LIGHT_PURPLE;", ChatColor.LIGHT_PURPLE.toString());
		announce = announce.replaceAll("&RED;", ChatColor.RED.toString());
		announce = announce.replaceAll("&WHITE;", ChatColor.WHITE.toString());
		announce = announce.replaceAll("&YELLOW;", ChatColor.YELLOW.toString());
		announce = announce.replaceAll("&0", ChatColor.BLACK.toString());
		announce = announce.replaceAll("&1", ChatColor.DARK_BLUE.toString());
		announce = announce.replaceAll("&2", ChatColor.DARK_GREEN.toString());
		announce = announce.replaceAll("&3", ChatColor.DARK_AQUA.toString());
		announce = announce.replaceAll("&9", ChatColor.BLUE.toString());
		announce = announce.replaceAll("&8", ChatColor.DARK_GRAY.toString());
		announce = announce.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
		announce = announce.replaceAll("&4", ChatColor.DARK_RED.toString());
		announce = announce.replaceAll("&6", ChatColor.GOLD.toString());
		announce = announce.replaceAll("&7", ChatColor.GRAY.toString());
		announce = announce.replaceAll("&a", ChatColor.GREEN.toString());
		announce = announce.replaceAll("&b", ChatColor.AQUA.toString());
		announce = announce.replaceAll("&c", ChatColor.RED.toString());
		announce = announce.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
		announce = announce.replaceAll("&e", ChatColor.YELLOW.toString());
		announce = announce.replaceAll("&f", ChatColor.WHITE.toString());
		announce = announce.replaceAll("&r", ChatColor.RESET.toString());
		return announce;
	}

	public static double round(double value, int decimalPlace) {
		double power_of_ten = 1.0D;

		double fudge_factor = 0.05D;
		while (decimalPlace-- > 0) {
			power_of_ten *= 10.0D;
			fudge_factor /= 10.0D;
		}
		return Math.round((value + fudge_factor) * power_of_ten) / power_of_ten;
	}

	public static double longToHours(long l) {
		
		return ((((l)/1000)/60)/60);
	}
	
}
