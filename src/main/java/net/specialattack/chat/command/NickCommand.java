package net.specialattack.chat.command;

import java.util.List;
import net.specialattack.chat.Consts;
import net.specialattack.chat.SpAChat;
import net.specialattack.chat.Util;
import net.specialattack.spacore.api.command.AbstractEasyCommand;
import net.specialattack.spacore.api.command.CommandException;
import net.specialattack.spacore.api.command.parameter.AbstractEasyParameter;
import net.specialattack.spacore.api.command.parameter.StringEasyParameter;
import net.specialattack.spacore.util.ChatFormat;
import net.specialattack.spacore.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class NickCommand extends AbstractEasyCommand implements CommandExecutor, TabCompleter {

    private final SpAChat plugin;
    private String permission;

    private final AbstractEasyParameter<String> nickname;

    public NickCommand(SpAChat plugin, String permissions) {
        this.plugin = plugin;
        this.permission = permissions;
        this.addParameter(this.nickname = new StringEasyParameter());
        this.finish();
    }

    @Override
    public void runCommand(CommandSender sender) {
        Player player = (Player) sender;

        String nickname = this.nickname.get();
        if (nickname == null || nickname.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Invalid use, need to set a nickname");
            return;
        }
        if (nickname.equals("UNSET")) {
            this.plugin.nicknames.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Nickname removed!");
            ChatUtil.sendToAll(p -> p.hasPermission(""), "");
        } else {
            nickname = Util.colorize(nickname);
            this.plugin.nicknames.put(player.getUniqueId(), nickname);
            player.sendMessage(ChatFormat.format("Nickname set to %s", ChatColor.GREEN, (player.hasPermission(Consts.PERM_CUSTOM_NICK) ? nickname : ChatColor.stripColor(nickname))));
            if (!player.hasPermission(Consts.PERM_CUSTOM_NICK)) {
                player.sendMessage(ChatColor.RED + "Note: you do not have permission to display this nickname");
            }
        }
        this.plugin.updatePlayerNickname(player);
        this.plugin.saveNicknames();
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    public boolean canUseCommand(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        try {
            if (!this.canUseCommand(sender)) {
                sender.sendMessage(ChatColor.RED + "You cannot use this command.");
                return true;
            }

            if (!this.hasPermission(sender)) {
                sender.sendMessage(ChatColor.RED + "You do not have permissions to use this command.");
                return true;
            }

            this.parseParameters(sender, alias, args);
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.DARK_RED + "An error occoured while performing command");
            sender.sendMessage(ChatFormat.format(e.message, ChatColor.RED, e.params));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occoured while performing command");
            sender.sendMessage(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return ChatUtil.TAB_RESULT_EMPTY;
    }
}
