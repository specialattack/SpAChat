package net.specialattack.chat;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NameFormatEvent extends Event {

	private String name;
	private List<Rank> ranks;

	public NameFormatEvent(String name, List<Rank> isIn) {
		this.name = name;
		this.ranks = isIn;
	}

	private static final HandlerList handlers = new HandlerList();

	public String getName() {
		return name;
	}

	public List<Rank> getRanks() {
		return ranks;
	}

	public void addRank(Rank r) {
		ranks.add(r);
	}

	public void setName(String name) {
		this.name = name;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
