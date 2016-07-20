package net.specialattack.chat.command;

import net.specialattack.chat.Consts;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.Util;
import net.specialattack.spacore.api.command.AbstractSubCommand;
import net.specialattack.spacore.api.command.ISubCommandHolder;
import net.specialattack.spacore.api.command.parameter.AbstractEasyParameter;
import net.specialattack.spacore.api.command.parameter.OfflinePlayerEasyParameter;
import net.specialattack.spacore.api.command.parameter.StringEasyParameter;
import net.specialattack.spacore.util.ChatFormat;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickSubCommand extends AbstractSubCommand {

    private final SpAChat plugin;

    private final AbstractEasyParameter<OfflinePlayer> player;
    private final AbstractEasyParameter<String> nickname;

    public NickSubCommand(ISubCommandHolder command, SpAChat plugin, String name, String permission, String... aliases) {
        super(command, name, permission, aliases);
        this.plugin = plugin;
        this.addParameter(this.player = new OfflinePlayerEasyParameter());
        this.addParameter(this.nickname = new StringEasyParameter());
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        OfflinePlayer player = this.player.get();
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Could not find the player");
            return;
        }

        String nickname = this.nickname.get();
        if (nickname == null || nickname.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Invalid use, need to set a nickname");
            return;
        }
        if (nickname.equals("UNSET")) {
            this.plugin.nicknames.remove(player.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Nickname removed!");
        } else {
            nickname = Util.colorize(nickname);
            this.plugin.nicknames.put(player.getUniqueId(), nickname);
            sender.sendMessage(ChatFormat.format("Nickname set to %s", ChatColor.GREEN, nickname));
            if (!sender.hasPermission(Consts.PERM_CUSTOM_NICK)) {
                sender.sendMessage(ChatColor.RED + "Note: you do not have permission to display this nickname");
            }
        }
        if (player instanceof Player) {
            this.plugin.updatePlayerNickname((Player) player);
        }
        this.plugin.saveNicknames();
    }
}
