
package net.specialattack.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventHandler implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        SpAChat.instance.formatMessage(event);
    }

}
