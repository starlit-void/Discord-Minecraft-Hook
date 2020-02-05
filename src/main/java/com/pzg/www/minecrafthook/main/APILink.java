package com.pzg.www.minecrafthook.main;

import org.bukkit.plugin.Plugin;

import com.pzg.www.api.commands.Command;
import com.pzg.www.api.config.Config;

/**
 * The official way to link into the Discord Minecraft Hook Bot.
 * @author TJPlaysNow
 * @version 2.4
 */
public class APILink {
	
	private PluginMain pluginMain;
	
	/**
	 * Get a new instance of the Discord Minecraft Hook Bot API.
	 * @param plugin The plugin accessing the Discord Minecraft Hook Bot API.
	 */
	public APILink(Plugin plugin) {
		System.out.println("[INFO] " + plugin.getName() + " is connecting to the Discord Minecraft Hook bot API.");
		pluginMain = PluginMain.getInstance();
	}
	
	/**
	 * Get the bot.
	 * @return MinecraftHookBot
	 */
	public MinecraftHookBot getBot() {
		return pluginMain.getBot();
	}
	
	/**
	 * Add a Minecraft command to /mch and /minecrafthook.
	 * @param command The command to add.
	 */
	public void addMinecraftCommand(Command command) {
		pluginMain.addCommand(command);
	}
	
	/**
	 * Get the Discord Minecraft Hook Bot configuration file.
	 * @return Config
	 */
	public Config getConfig() {
		return pluginMain.getMCHConfig();
	}
}