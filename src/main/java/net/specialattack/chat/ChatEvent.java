
package net.specialattack.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
            event.setFormat(this.plugin.getFormattedChatMessage(p, event.getMessage()));
        }
        catch (Exception e) {

        }
    }

}
