package com.pzg.www.minecrafthook.object;

import java.util.UUID;

/**
 * @author TJPlaysNow
 * @version 2.0
 */
public class User {

    private UUID minecraftUUID;
    private long discordID;

    /**
     * Create a new user.
     *
     * @param minecraftUUID Minecraft Player's UUID.
     * @param discordID     Discord User's ID.
     */
    public User(UUID minecraftUUID, long discordID) {
        this.minecraftUUID = minecraftUUID;
        this.discordID = discordID;
    }

    /**
     * Gets the user's Minecraft Player's UUID.
     *
     * @return Minecraft Player's UUID.
     */
    public UUID getMinecraftUUID() {
        return minecraftUUID;
    }

    /**
     * Gets the user's Discord ID.
     *
     * @return Discord User's ID.
     */
    public long getDiscordID() {
        return discordID;
    }
}
