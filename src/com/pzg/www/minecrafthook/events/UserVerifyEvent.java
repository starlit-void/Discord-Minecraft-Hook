package com.pzg.www.minecrafthook.events;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
public class UserVerifyEvent extends Event {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private IUser user;
	private IGuild guild;
	private UUID minecraftPlayer;
	
	public UserVerifyEvent(IGuild guild, IUser user, UUID minecraftPlayer) {
		this.guild = guild;
		this.user = user;
		this.minecraftPlayer = minecraftPlayer;
	}
	
	public IUser getUser() {
		return user;
	}
	
	public IGuild getGuild() {
		return guild;
	}
	
	public UUID getMinecraftPlayerUUID() {
		return minecraftPlayer;
	}
	
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}