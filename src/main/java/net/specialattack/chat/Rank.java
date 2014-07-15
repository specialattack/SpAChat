
package net.specialattack.chat;

public class Rank {

    public String name;
    public String prefix;
    public String suffix;
    public int importance;

    public Rank(String name) {
        this.name = name;
    }

    public void init(String prefix, int importance) {
        this.prefix = prefix;
        this.importance = importance;
    }

    public String getPermissionNode() {
        return "spachat." + this.name;
    }

    public String getFormattedName(String name) {
        return this.prefix + name;
    }

}
