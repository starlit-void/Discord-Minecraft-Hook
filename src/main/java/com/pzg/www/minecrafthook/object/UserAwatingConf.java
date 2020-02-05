package com.pzg.www.minecrafthook.object;

import java.util.UUID;

import com.pzg.www.api.random.RandomString;

public class UserAwatingConf {

    private UUID playerUUID;
    private String random;

    /**
     * Creates a new user awaiting confirmation.
     *
     * @param playerUUID
     */
    public UserAwatingConf(UUID playerUUID) {
        this.playerUUID = playerUUID;
        random = new RandomString(8).nextString();
    }

    /**
     * Loads a user from a config that is awaiting confirmation.
     *
     * @param playerUUID
     * @param random
     */
    public UserAwatingConf(UUID playerUUID, String random) {
        this.playerUUID = playerUUID;
        this.random = random;
    }

    /**
     * Get the user's Minecraft UUID.
     *
     * @return Player's Minecraft UUID.
     */
    public UUID getMinecraftUUID() {
        return playerUUID;
    }

    /**
     * Get the string the player must type to confirm.
     *
     * @return Player's confirmation string.
     */
    public String getRandom() {
        return random;
    }
}
