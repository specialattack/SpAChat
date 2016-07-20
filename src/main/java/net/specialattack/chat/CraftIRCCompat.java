package net.specialattack.chat;

import com.ensifera.animosity.craftirc.CraftIRC;
import com.ensifera.animosity.craftirc.RelayedMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class CraftIRCCompat {

    private CraftIRCCompat() {
    }

    public static void handleCraftIRC(Plugin craftIRC, Player player, String message) {
        if (!craftIRC.isEnabled()) {
            return;
        }
        CraftIRC plugin = (CraftIRC) craftIRC;
        if (!plugin.isHeld(CraftIRC.HoldType.CHAT)) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
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
            });
        }
    }
}
