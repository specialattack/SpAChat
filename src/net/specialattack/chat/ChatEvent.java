package net.specialattack.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatEvent implements Listener {

	public ChatMain plugin;

	public ChatEvent(ChatMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
			}
		Player p = event.getPlayer();
		String name = p.getName();
		try {
			event.setFormat(plugin.getFormattedChatMessage(p, event.getMessage()));
		} catch (Exception e) {

		}
	}

}
