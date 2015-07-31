package net.specialattack.chat.command;

import java.util.logging.Level;
import net.specialattack.bukkit.core.command.AbstractSubCommand;
import net.specialattack.bukkit.core.command.ISubCommandHolder;
import net.specialattack.chat.SpAChat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends AbstractSubCommand {

    public ReloadSubCommand(ISubCommandHolder command, String name, String permissions, String... aliases) {
        super(command, name, permissions, aliases);
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        try {
            SpAChat.instance.loadConfig();
            sender.sendMessage(ChatColor.GREEN + "SpAChat config reloaded!");
        } catch (Exception e) {
            SpAChat.log(Level.SEVERE, "Error loading SpAChat config", e);
            sender.sendMessage(ChatColor.RED + "There was a problem reloading the SpAChat config. Please check the console for more information.");
        }
    }

}
