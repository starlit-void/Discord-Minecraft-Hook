package com.pzg.www.minecrafthook.object;

import discord4j.core.object.util.Snowflake;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The user list object.
 *
 * @author TJPlaysNow
 * @version 2.0
 */
public class UserList {

    private List<User> users = new ArrayList<>();
    private List<UserAwatingConf> awaitingConfUsers = new ArrayList<>();

    /**
     * Loads formatted data from a String list.
     *
     * @param formattedUsers String list with users.
     */
    public void loadUsers(List<String> formattedUsers) {
        for (String formattedUser : formattedUsers) {
            String[] part = formattedUser.split(" : ");
            User newUser = new User(UUID.fromString(part[0]), Snowflake.of(Long.parseLong(part[1])));
            users.add(newUser);
            System.out.println("Added user:");
            System.out.println("  - Minecraft UUID: " + newUser.getMinecraftUUID());
            System.out.println("  - Discord ID:     " + newUser.getDiscordID());
        }
    }

    /**
     * Loads formatted data from a String list.
     *
     * @param formattedUsers String list with users awaiting confirmation.
     */
    public void loadUsersAwaitingConf(List<String> formattedUsers) {
        for (String formattedUser : formattedUsers) {
            String[] part = formattedUser.split(" : ");
            awaitingConfUsers.add(new UserAwatingConf(UUID.fromString(part[0]), part[1]));
        }
    }

    /**
     * Get's the users awaiting confirmation as a String list to write to a config.
     *
     * @return A formatted list for saving to a config.
     */
    public List<String> getUsersAwaitingConfStringList() {
        List<String> formattedUsers = new ArrayList<>();
        for (UserAwatingConf user : awaitingConfUsers) {
            String formattedUser = user.getMinecraftUUID() + " : " + user.getRandom();
            formattedUsers.add(formattedUser);
        }
        return formattedUsers;
    }

    /**
     * Get's the users list as a String list to write to a config.
     *
     * @return A formatted list for saving to a config.
     */
    public List<String> getUsersStringList() {
        List<String> formattedUsers = new ArrayList<>();
        for (User user : users) {
            String formattedUser = user.getMinecraftUUID() + " : " + user.getDiscordID();
            formattedUsers.add(formattedUser);
        }
        return formattedUsers;
    }

    /**
     * Get's the users awaiting confirmation.
     *
     * @return UserAwatingConf
     */
    public List<UserAwatingConf> getUsersAwaitingConf() {
        return awaitingConfUsers;
    }

    /**
     * Returns a user with the minecraft uuid.
     *
     * @param minecraftUUID The players UUID.
     * @return The user object or null.
     * @throws NullPointerException If the user wasn't found null.
     */
    public User getUser(UUID minecraftUUID) throws NullPointerException {
        for (User user : users) {
            if (user.getMinecraftUUID().equals(minecraftUUID)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns a user with the discord id.
     *
     * @param discordID
     * @return The user object or null.
     * @throws NullPointerException If the user wasn't found null.
     */
    public User getUser(Snowflake discordID) throws NullPointerException {
        for (User user : users) {
            if (user.getDiscordID() == discordID) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns a user with the minecraft uuid.
     *
     * @param minecraftUUID The players UUID.
     * @return The user object or null.
     * @throws NullPointerException If the user wasn't found null.
     */
    public UserAwatingConf getUserAwaitingConf(UUID minecraftUUID) throws NullPointerException {
        for (UserAwatingConf user : awaitingConfUsers) {
            if (user.getMinecraftUUID() == minecraftUUID) {
                return user;
            }
        }
        return null;
    }

    /**
     * Adds an awaiting user to the list.
     *
     * @param minecraftUUID Minecraft Player's UUID.
     */
    public void addUser(UUID minecraftUUID) {
        UserAwatingConf user = new UserAwatingConf(minecraftUUID);
        awaitingConfUsers.add(user);
    }

    /**
     * Verifies the user and adds him to the official list.
     *
     * @param random    The string for verify command.
     * @param discordID The user's Discord ID.
     */
    public void verifyUser(String random, Snowflake discordID) {
        UserAwatingConf oldUser = null;
        for (UserAwatingConf user : awaitingConfUsers) {
            if (user.getRandom().equalsIgnoreCase(random)) {
                User u = new User(user.getMinecraftUUID(), discordID);
                users.add(u);
                oldUser = user;
            }
        }
        if (oldUser != null)
            awaitingConfUsers.remove(oldUser);
    }
}
