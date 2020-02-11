package com.pzg.www.minecrafthook.main;

import com.pzg.www.api.config.Config;
import com.pzg.www.minecrafthook.object.UserList;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import reactor.util.annotation.Nullable;

import java.util.List;

/**
 * The Minecraft Hook Bot object.
 *
 * @author TJPlaysNow
 * @version 2.0
 */
public class MinecraftHookBot {

    protected DiscordClient client;
    protected UserList users;
    private Config userConf;

    /**
     * Create a new Minecraft Hook Bot user.
     *
     * @param token  The bot token.
     * @param prefix The bot's prefix.
     */
    public MinecraftHookBot(String token, String prefix) {
        client = new DiscordClientBuilder(token).build();
        userConf = new Config("plugins/Minecraft Hook", "Users.yml", () -> {
        }, PluginMain.plugin);

        users = new UserList();

        userConf.getConfig().getStringList("Users");
        users.loadUsers(userConf.getConfig().getStringList("Users"));

        userConf.getConfig().getStringList("UsersAwaitingConfirmation");
        users.loadUsersAwaitingConf(userConf.getConfig().getStringList("UsersAwaitingConfirmation"));
    }

    /**
     * The method ran when the server is shutdown.
     */
    public void disable() {
        userConf.getConfig().set("Users", users.getUsersStringList());
        userConf.getConfig().set("UsersAwaitingConfirmation", users.getUsersAwaitingConfStringList());
        userConf.saveConfig();
    }

    /**
     * Get the registered users.
     *
     * @return UserList
     */
    public UserList getUsers() {
        return users;
    }

    @Nullable
    public List<Guild> getGuilds() {
        return client.getGuilds().collectList().block();
    }
}
