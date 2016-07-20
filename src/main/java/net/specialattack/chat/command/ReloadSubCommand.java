package net.specialattack.chat.command;

import java.util.logging.Level;
import net.specialattack.chat.SpAChat;
import net.specialattack.spacore.api.command.AbstractSubCommand;
import net.specialattack.spacore.api.command.ISubCommandHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends AbstractSubCommand {

    private final SpAChat plugin;

    public ReloadSubCommand(ISubCommandHolder command, SpAChat plugin, String name, String permissions, String... aliases) {
        super(command, name, permissions, aliases);
        this.plugin = plugin;
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        try {
            this.plugin.loadConfig();
            sender.sendMessage(ChatColor.GREEN + "SpAChat config reloaded!");
        } catch (Exception e) {
            this.plugin.log(Level.SEVERE, "Error loading SpAChat config", e);
            sender.sendMessage(ChatColor.RED + "There was a problem reloading the SpAChat config. Please check the console for more information.");
        }
    }
}
