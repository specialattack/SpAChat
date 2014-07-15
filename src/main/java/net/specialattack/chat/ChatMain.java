
package net.specialattack.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMain extends JavaPlugin {

    public List<String> ranks = new ArrayList<String>();
    public List<Rank> rankProfiles = new ArrayList<Rank>();
    public String format = "&health[&world]&name: &msg";
    public HashMap<String, String> worldAlias = new HashMap<String, String>();

    @Override
    public void onEnable() {
        try {
            this.getConfig().load("plugins/SpAChat/config.yml");
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.initRanks();
        this.format = this.getConfig().getString("chatformat", this.format);
        this.createWorldAlias();
        try {
            this.getConfig().save("plugins/SpAChat/config.yml");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new ChatEvent(this), this);

    }

    @Override
    public void onDisable() {

    }

    private void createWorldAlias() {
        List<?> list = this.getConfig().getStringList("worldAlias");
        for (int i = 0; i < list.size(); i++) {
            String aliasNode = list.get(i) + "";
            String[] pair = aliasNode.split("-");
            this.worldAlias.put(pair[0].trim().toLowerCase(), pair[1].trim());
        }
    }

    public void initRanks() {
        this.ranks = this.getConfig().getStringList("rankList");

        for (String rank : this.ranks) {
            Rank r = new Rank(rank);
            r.init(this.getConfig().getString("ranks." + rank + ".prefix"), this.getConfig().getInt("ranks." + rank + ".priority"));
            this.rankProfiles.add(r);
        }
    }

    public String getFormattedName(Player p) {
        List<Rank> isIn = new ArrayList<Rank>();
        for (Rank r : this.rankProfiles) {
            if (p.hasPermission(r.getPermissionNode())) {
                isIn.add(r);
            }
        }

        String name = p.getName();
        Event event = new NameFormatEvent(name, isIn);
        Bukkit.getPluginManager().callEvent(event);

        isIn = ((NameFormatEvent) event).getRanks();

        Collections.sort(isIn, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                if (o1.importance < o2.importance) {
                    return 1;
                }
                else if (o1.importance > o2.importance) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        for (Rank r : isIn) {
            name = r.getFormattedName(name);
        }

        name = Util.colorize(name);
        return name;
    }

    public String getFormattedChatMessage(Player p, String message) {
        String msg = this.format.replace("&name", this.getFormattedName(p)).replace("&world", this.getWorld(p)); //.replace("&health", getHealthBar(p.getHealth()))
        return Util.colorize(msg).replace("&msg", message);
    }

    private String getWorld(Player p) {
        String worldname = p.getWorld().getName().toLowerCase();
        if (this.worldAlias.containsKey(worldname)) {
            worldname = this.worldAlias.get(worldname);
        }
        return worldname;
    }

    private String getHealthBar(int health) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i += 2) {
            if (health > i) {
                sb.append(ChatColor.DARK_GREEN + "|");
            }
            else {
                sb.append(ChatColor.DARK_RED + "|");
            }
        }
        return sb.toString();
    }

}
