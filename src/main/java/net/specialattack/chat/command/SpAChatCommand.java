package net.specialattack.chat.command;

import net.specialattack.chat.Consts;
import net.specialattack.chat.SpAChat;
import net.specialattack.spacore.api.command.AbstractMultiCommand;
import net.specialattack.spacore.command.HelpSubCommand;
import net.specialattack.spacore.command.VersionSubCommand;

public class SpAChatCommand extends AbstractMultiCommand {

    public SpAChatCommand(SpAChat plugin) {
        new VersionSubCommand(this, plugin.getDescription(), "version", Consts.PERM_COMMAND_SPACHAT_VERSION);
        new HelpSubCommand(this, "help", Consts.PERM_COMMAND_SPACHAT_HELP, "?");
        new ReloadSubCommand(this, plugin, "reload", Consts.PERM_COMMAND_SPACHAT_RELOAD);
        new NickSubCommand(this, plugin, "nick", Consts.PERM_COMMAND_SPACHAT_NICK);
    }

    @Override
    public String getDefaultCommand() {
        return "version";
    }
}
