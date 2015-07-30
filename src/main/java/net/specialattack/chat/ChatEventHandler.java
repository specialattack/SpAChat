package net.specialattack.chat;

import com.ensifera.animosity.craftirc.CraftIRC;
import com.ensifera.animosity.craftirc.RelayedMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (SpAChat.instance.format != null) {
            Bukkit.spigot().broadcast(SpAChat.instance.format.performFormat(event.getPlayer(), event.getMessage()));
            event.setCancelled(true);
            if (SpAChat.instance.craftIRC != null && SpAChat.craftIRCFix) {
                this.handleCraftIRC(event.getPlayer(), event.getMessage());
            }
        }
    }

    public void handleCraftIRC(final Player player, final String message) {
        final CraftIRC plugin = (CraftIRC) SpAChat.instance.craftIRC;
        if (!plugin.isHeld(CraftIRC.HoldType.CHAT)) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    RelayedMessage msg;
                    msg = plugin.newMsg(plugin.getEndPoint(plugin.cMinecraftTag()), null, "chat");

                    if (msg != null) {
                        msg.setField("sender", player.getDisplayName());
                        msg.setField("message", message);
                        msg.setField("world", player.getWorld().getName());
                        msg.setField("realSender", player.getName());
                        msg.setField("prefix", plugin.getPrefix(player));
                        msg.setField("suffix", plugin.getSuffix(player));
                        msg.doNotColor("message");
                        msg.post();
                    }
                }
            });
        }
    }

}
