package net.specialattack.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.specialattack.spacore.event.PlayerPermissionsChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class Handler implements Listener {

    private final SpAChat plugin;
    private Plugin craftIRC;

    public Handler(SpAChat plugin) {
        this.plugin = plugin;
        this.craftIRC = plugin.getServer().getPluginManager().getPlugin("CraftIRC");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPermissionsChanged(PlayerPermissionsChangedEvent event) {
        this.plugin.updatePlayerNickname(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        this.plugin.updatePlayerNickname(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Format format = this.plugin.tagManager.format;
        if (format != null) {
            BaseComponent message = format.performFormat(this.plugin, event.getPlayer(), event.getMessage());
            event.setCancelled(true);
            Bukkit.getConsoleSender().sendMessage(message.toLegacyText());
            Bukkit.spigot().broadcast(message);
            if (this.plugin.craftIRCFix && this.craftIRC != null) {
                CraftIRCCompat.handleCraftIRC(this.craftIRC, event.getPlayer(), event.getMessage());
            }
        }
    }
}
