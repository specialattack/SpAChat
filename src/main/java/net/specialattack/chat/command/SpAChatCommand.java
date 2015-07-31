package net.specialattack.chat.command;

import net.specialattack.bukkit.core.command.AbstractMultiCommand;
import net.specialattack.bukkit.core.command.HelpSubCommand;
import net.specialattack.bukkit.core.command.VersionSubCommand;
import net.specialattack.chat.SpAChat;

public class SpAChatCommand extends AbstractMultiCommand {

    public SpAChatCommand() {
        new VersionSubCommand(this, SpAChat.instance.getDescription(), "version", "spachat.command.version");
        new HelpSubCommand(this, "help", "spachat.command.help", "?");
        new ReloadSubCommand(this, "reload", "spachat.command.reload");
    }

    @Override
    public String getDefaultCommand() {
        return "version";
    }

}
