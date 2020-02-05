package com.pzg.www.minecrafthook.events;

import java.util.UUID;

import discord4j.core.DiscordClient;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserVerifyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private UUID minecraftPlayer;

    public UserVerifyEvent(UUID minecraftPlayer) {
        this.minecraftPlayer = minecraftPlayer;
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
