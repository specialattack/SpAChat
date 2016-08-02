package net.specialattack.chat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.specialattack.spacore.event.PlayerPermissionsChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Handler implements Listener {

    private final SpAChat plugin;
    private Plugin craftIRC;

    private Map<UUID, String> playerMessages = new HashMap<>();

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.playerMessages.containsKey(uuid)) {
            Server server = this.plugin.getServer();
            server.getPluginManager().callEvent(new AsyncPlayerChatEvent(false, event.getPlayer(), this.playerMessages.remove(uuid).trim(), new HashSet<>(server.getOnlinePlayers())));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChatPre(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String message = event.getMessage();
        if (this.playerMessages.containsKey(uuid)) {
            message = this.playerMessages.get(uuid) + " " + message;
            event.setMessage(message);
        }
        if (event.getMessage().endsWith("\\")) {
            this.playerMessages.put(uuid, message.substring(0, message.length() - 1));
            event.setCancelled(true);
        } else {
            this.playerMessages.remove(uuid);
        }
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
