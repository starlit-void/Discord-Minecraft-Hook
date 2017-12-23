package com.pzg.www.minecrafthook.main;

import java.util.List;

import com.pzg.www.api.config.Config;
import com.pzg.www.api.config.ConfigCreate;
import com.pzg.www.discord.object.Bot;
import com.pzg.www.discord.object.CommandMethod;
import com.pzg.www.minecrafthook.object.UserList;

import sx.blah.discord.handle.obj.IGuild;

/**
 * The Minecraft Hook Bot object.
 * @author TJPlaysNow
 * @version 2.0
 */
public class MinecraftHookBot {
	
	protected Bot bot;
	protected UserList users;
	private Config userConf;
	
	/**
	 * Create a new Minecraft Hook Bot user.
	 * @param token The bot token.
	 * @param prefix The bot's prefix.
	 */
	public MinecraftHookBot(String token, String prefix) {
		bot = new Bot(token, prefix);
		userConf = new Config("plugins/Minecraft Hook", "Users.yml", new ConfigCreate() {
			@Override
			public void configCreate() {
				
			}
		}, PluginMain.plugin);
		
		users = new UserList();
		
		if (userConf.getConfig().getStringList("Users") != null) {
			users.loadUsers(userConf.getConfig().getStringList("Users"));
		}
		
		if (userConf.getConfig().getStringList("UsersAwaitingConfirmation") != null) {
			users.loadUsersAwaitingConf(userConf.getConfig().getStringList("UsersAwaitingConfirmation"));
		}
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
	 * Get the bot's command prefix.
	 * @return The bot's prefix as String.
	 */
	public String getPrefix() {
		return bot.getPrefix();
	}
	
	/**
	 * Get a list of guilds the bot is in.
	 * @return List of guilds the bot is in.
	 */
	public List<IGuild> getGuilds() {
		return bot.getBot().getGuilds();
	}
	
	/**
	 * Get the registered users.
	 * @return UserList
	 */
	public UserList getUsers() {
		return users;
	}
	
	/**
	 * Add a command to the bot.
	 * @param command The command to add.
	 */
	public void addCommand(CommandMethod command) {
		bot.addCommand(command);
	}
	
	/**
	 * Remove a command from the bot.
	 * @param command The command to remove.
	 */
	public void removeCommand(CommandMethod command) {
		bot.removeCommand(command);
	}
	
	/**
	 * Add a bad word to the bot so users can't say it.
	 * @param badWord The bad word to add.
	 */
	public void addBadWord(String badWord) {
		bot.addBadWord(badWord);
	}
	
	/**
	 * Remove a bad word from the bot so users can say it.
	 * @param badWord The bad word to remove.
	 */
	public void removeBadWord(String badWord) {
		bot.removeBadWord(badWord);
	}
}