package com.pzg.www.minecrafthook.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.pzg.www.api.commands.Command;
import com.pzg.www.api.commands.CommandManager;
import com.pzg.www.api.commands.CommandMethod;
import com.pzg.www.api.config.Config;
import com.pzg.www.api.config.ConfigCreate;
import com.pzg.www.discord.object.Method;
import com.pzg.www.minecrafthook.events.UserVerifyEvent;
import com.pzg.www.minecrafthook.listeners.ChatListener;
import com.pzg.www.minecrafthook.object.UserAwatingConf;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.PermissionUtils;

public class PluginMain extends JavaPlugin {
	
	public final String version = "v2.7";
	
	public static Plugin plugin;
	
	private static MinecraftHookBot bot;
	
	private Config config;
	private boolean configCreated = true;

	private CommandManager minecrafthook;
	private CommandManager mch;
	
	public static PluginMain pluginMain;
	
	public PluginMain() {
		pluginMain = this;
	}
	
	@Override
	public void onEnable() {
//		Default variable initialization.
		plugin = this;
		minecrafthook = new CommandManager("minecrafthook");
		getCommand("minecrafthook").setExecutor(minecrafthook);
		mch = new CommandManager("mch");
		getCommand("mch").setExecutor(mch);
		
//		Loading configuration files.
		config = new Config("plugins/Minecraft Hook", "Config.yml", new ConfigCreate() {
			@Override
			public void configCreate() {
				configCreated = false;
			}
		}, plugin);
		
		if (!configCreated) {
			config.getConfig().set("Version", version);
			
			config.getConfig().set("Discord.Bot.ClientID", "<Discord Client ID>");
			config.getConfig().set("Discord.Bot.Token", "<Bot User Token>");
			config.getConfig().set("Discord.Bot.Prefix", "!");
			
			config.getConfig().set("Discord.Role.Verified", "Verified");
			
			config.getConfig().set("Discord.Command.Verify", true);
			config.getConfig().set("Discord.Command.Help", true);
			config.getConfig().set("Discord.Command.Clear", true);
			config.getConfig().set("Discord.Command.Mute", true);
			config.getConfig().set("Discord.Command.Unmute", true);
			config.getConfig().set("Discord.Command.Custom", true);
			config.getConfig().set("Discord.Commands.Test", "This is a test command.");

			config.getConfig().set("Discord.Chat.Sync", true);
			config.getConfig().set("Discord.Chat.Message.Format", "&6Discord | &b#{CHANNEL} §e{USER} §7: §f{MESSAGE}");
			
			List<String> channels = new ArrayList<String>();
			channels.add("general");
			channels.add("offtopic");
			
			config.getConfig().set("Discord.Channels.Chat", channels);
			
			config.getConfig().set("Minecraft.Command.Help", true);
			config.getConfig().set("Minecraft.Command.Verify", true);

			config.getConfig().set("Minecraft.Chat.Sync.Messages", true);
			config.getConfig().set("Minecraft.Chat.Sync.Deaths", true);
			config.getConfig().set("Minecraft.Chat.Sync.Join", true);
			config.getConfig().set("Minecraft.Chat.Sync.Leave", true);
			config.getConfig().set("Minecraft.Chat.Sync.SayCommand", true);
			config.getConfig().set("Minecraft.Chat.Sync.Server.Start", true);
			config.getConfig().set("Minecraft.Chat.Sync.Server.Stop", true);
			config.getConfig().set("Minecraft.Chat.Message.Embed", true);
			config.getConfig().set("Minecraft.Chat.Message.Format", "{DATE} - &6{PLAYER}&r: {MESSAGE}");
			
			config.getConfig().set("Minecraft.Channel.Chat", "minecraft-chat");

			config.getConfig().set("Minecraft.Command.Message.Error", "&cUh oh, you don't have the permissions to do that!");
			
			config.saveConfig();
			Bukkit.getLogger().log(Level.WARNING, "Created a new config. Please go and edit it before reloading the server.");
		} else {
			if (!config.getConfig().getString("Version").equalsIgnoreCase(version)) {
				Bukkit.getLogger().log(Level.SEVERE, "Backup and remove your configuration files, then restart.");
				Bukkit.getPluginManager().disablePlugin(plugin);
			} else {
//				Initializing the bot.
				bot = new MinecraftHookBot(config.getConfig().getString("Discord.Bot.Token"), config.getConfig().getString("Discord.Bot.Prefix"));
				
//				Minecraft commands.
				if (config.getConfig().getBoolean("Minecraft.Command.Verify")) {
					minecrafthook.addCommand(new Command("verify", "minecrafthook.verify", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "§"), new CommandMethod() {
						@Override
						public boolean run(CommandSender sender, String[] args) {
							if (sender instanceof Player) {
								if (bot.users.getUser(((Player) sender).getUniqueId()) != null) {
									sender.sendMessage(new String[] {	"§7--------------------",
																		"§cYou're already verified!",
																		"§6Account: " + bot.bot.getBot().getUserByID(bot.users.getUser(((Player) sender).getUniqueId()).getDiscordID()).getName(),
																		"§7--------------------"});
									return true;
								} else {
									if (bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()) == null )
										bot.users.addUser(((Player) sender).getUniqueId());
									sender.sendMessage(new String[] {	"§7--------------------",
																		"§b Please type the command in discord:",
																		"§6  - '!verify " + bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()).getRandom() + "'",
																		"§7--------------------"});
									return true;
								}} else
									sender.sendMessage("§cUh oh, this is a player only command!");
							return false;
						}
					}));
					mch.addCommand(new Command("verify", "minecrafthook.verify", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "§"), new CommandMethod() {
						@Override
						public boolean run(CommandSender sender, String[] args) {
							if (sender instanceof Player) {
								if (bot.users.getUser(((Player) sender).getUniqueId()) != null) {
									System.out.println("User found.");
									sender.sendMessage(new String[] {	"§7--------------------",
																		"§cYou're already verified!",
																		"§6Account: " + bot.bot.getBot().getUserByID(bot.users.getUser(((Player) sender).getUniqueId()).getDiscordID()).getName(),
																		"§7--------------------"});
									return true;
								} else {
									if (bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()) == null )
										bot.users.addUser(((Player) sender).getUniqueId());
									sender.sendMessage(new String[] {	"§7--------------------",
																		"§b Please type the command in discord:",
																		"§6  - '!verify " + bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()).getRandom() + "'",
																		"§7--------------------"});
									return true;
								}} else
									sender.sendMessage("§cUh oh, this is a player only command!");
							return false;
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Minecraft.Command.Help")) {
					minecrafthook.addCommand(new Command("help", "minecrafthook.help", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "§"), new CommandMethod() {
						@Override
						public boolean run(CommandSender sender, String[] args) {
							sender.sendMessage(new String[] {	"§7--------------------",
																"§6Commands:",
																"§6    + §7/minecrafthook verify",
																"§6      - Verify your discord account.",
																"§6      - Alt, §7/mch verify",
																"§6    + §7/minecrafthook help",
																"§6      - Send a help message.",
																"§6      - Alt, §7/mch help",
																"§7--------------------"});
							return true;
						}
					}));
					mch.addCommand(new Command("help", "minecrafthook.help", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "§"), new CommandMethod() {
						@Override
						public boolean run(CommandSender sender, String[] args) {
							sender.sendMessage(new String[] {	"§7--------------------",
																"§6Commands:",
																"§6    + §7/minecrafthook verify",
																"§6      - Verify your discord account.",
																"§6      - Alt, §7/mch verify",
																"§6    + §7/minecrafthook help",
																"§6      - Send a help message.",
																"§6      - Alt, §7/mch help",
																"§7--------------------"});
							return true;
						}
					}));
				}
				
//				Bot commands.
				if (config.getConfig().getBoolean("Discord.Command.Verify")) {
					bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod("verify", Permissions.SEND_MESSAGES.toString(), new Method() {
						@Override
						public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
							if (args.size() == 1) {
								UserAwatingConf useracc = null;
								for (UserAwatingConf userac : bot.users.getUsersAwaitingConf()) {
									if (userac.getRandom().equalsIgnoreCase(args.get(0))) {
										Calendar cal = Calendar.getInstance();
										EmbedBuilder eb = new EmbedBuilder();
										
										eb.withThumbnail("https://visage.surgeplay.com/head/208/" + userac.getMinecraftUUID().toString().replace("-", ""));

										eb.withColor(226, 244, 0);
										
										eb.appendField("**__Registered:__**", 
												"**__Minecraft Account__**: " + Bukkit.getOfflinePlayer(userac.getMinecraftUUID()).getName() +
												"\n**__Discord Account__**: " + user.getName(), true);
										
										String minute = "" + cal.get(Calendar.MINUTE);
										if (minute.length() == 1)
											minute = "0" + minute;
										
										eb.withFooterText(cal.get(Calendar.HOUR) + ":" + minute + " " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR));
										
										channel.sendMessage(eb.build());
										
										useracc = userac;
										
										UserVerifyEvent uve = new UserVerifyEvent(guild, user, userac.getMinecraftUUID());
										Bukkit.getPluginManager().callEvent(uve);
										
										for (IRole role : guild.getRoles()) {
											if (role.getName().equalsIgnoreCase(config.getConfig().getString("Discord.Role.Verified"))) {
												user.addRole(role);
											}
										}
										
									}
								}
								if (useracc != null) {
									bot.users.verifyUser(useracc.getRandom(), user.getLongID());
								}
							}
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Discord.Command.Help")) {
					bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod("help", Permissions.SEND_MESSAGES.toString(), new Method() {
						@Override
						public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
							String helpMessage = "**__Commands:__**" + "\n\n";
							
							if (config.getConfig().getBoolean("Discord.Command.Verify")) {
								helpMessage += "Verify" + "\n" + "  - Used to verify your minecraft account." + "\n\n";
							}
							
							if (config.getConfig().getBoolean("Discord.Command.Help")) {
								helpMessage += "Help" + "\n" + "  - Sends a list of commands." + "\n\n";
							}
							
							if (config.getConfig().getBoolean("Discord.Command.Mute")) {
								if (PermissionUtils.hasPermissions(guild, user, Permissions.ADMINISTRATOR)) {
									helpMessage += "Mute" + "\n" + "  - Mutes a specified user." + "\n\n";
								}
							}
							
							if (config.getConfig().getBoolean("Discord.Command.Unmute")) {
								if (PermissionUtils.hasPermissions(guild, user, Permissions.ADMINISTRATOR)) {
									helpMessage += "Unmute" + "\n" + "  - Unmutes a specified user." + "\n\n";
								}
							}
							
							if (config.getConfig().getBoolean("Discord.Command.Clear")) {
								if (PermissionUtils.hasPermissions(guild, user, Permissions.ADMINISTRATOR)) {
									helpMessage += "Clear" + "\n" + "  - Clears a specified amount of messages." + "\n\n";
								}
							}
							
							if (config.getConfig().getBoolean("Discord.Command.Custom")) {
								for (String com : config.getConfig().getConfigurationSection("Discord.Commands").getKeys(false)) {
									helpMessage += com + "\n" + "  - A custom message command." + "\n\n";
								}
							}
							
							user.getOrCreatePMChannel().sendMessage(helpMessage);
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Discord.Command.Clear")) {
					bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod("clear", Permissions.ADMINISTRATOR.toString(), new Method() {
						@Override
						public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
							if (args.size() == 1) {
								int size = Integer.parseInt(args.get(0));
								channel.getMessageHistory(size).bulkDelete();
							} else
								channel.sendMessage("Sorry try `!clear <number>`");
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Discord.Command.Mute")) {
					bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod("mute", Permissions.ADMINISTRATOR.toString(), new Method() {
						@Override
						public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
							if (args.size() == 1) {
								for (IUser muser : guild.getUsers()) {
									String mention = muser.mention().replace("@", "").replace("!", "");
									String arg = args.get(0).replace("@", "").replace("!", "");
									if (mention.equalsIgnoreCase(arg)) {
										bot.bot.muteUser(muser.getLongID());
									}
								}
							}
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Discord.Command.Unmute")) {
					bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod("unmute", Permissions.ADMINISTRATOR.toString(), new Method() {
						@Override
						public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
							if (args.size() == 1) {
								for (IUser muser : guild.getUsers()) {
									String mention = muser.mention().replace("@", "").replace("!", "");
									String arg = args.get(0).replace("@", "").replace("!", "");
									if (mention.equalsIgnoreCase(arg)) {
										bot.bot.unmuteUser(muser.getLongID());
									}
								}
							}
						}
					}));
				}
				
				if (config.getConfig().getBoolean("Discord.Command.Custom")) {
					for (String com : config.getConfig().getConfigurationSection("Discord.Commands").getKeys(false)) {
						bot.bot.addCommand(new com.pzg.www.discord.object.CommandMethod(com, Permissions.SEND_MESSAGES.toString(), new Method() {
							@Override
							public void method(IUser user, IChannel channel, IGuild guild, String label, List<String> args) {
								channel.sendMessage(config.getConfig().getString("Discord.Commands." + com));
							}
						}));
					}
				}
				
				if (config.getConfig().getBoolean("Minecraft.Chat.Sync")) {
					Bukkit.getPluginManager().registerEvents(new ChatListener(config.getConfig().getString("Minecraft.Chat.Channel"), config.getConfig().getString("Minecraft.Chat.Message.Format"), config.getConfig().getBoolean("Minecraft.Chat.Message.Embed")), plugin);
				}
				
				if (config.getConfig().getBoolean("Discord.Chat.Sync")) {
					bot.bot.setSendMethod(new Method() {
						@Override
						public void onMessage(MessageReceivedEvent event) {
							for (String channelName : config.getConfig().getStringList("Discord.Chat.Channels")) {
								if (event.getChannel().getName().equalsIgnoreCase(channelName)) {
									String userName = event.getAuthor().getName();
									String message = event.getMessage().getContent();
									Bukkit.broadcastMessage(config.getConfig().getString("Discord.Chat.Message.Format").replace("{CHANNEL}", channelName).replace("{USER}", userName).replace("{MESSAGE}", message).replace("&", "§"));
								}
							}
						}
					});
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		if (configCreated) {
			if (bot != null)
				bot.disable();
		}
	}
	
	public static PluginMain getInstance() {
		return pluginMain;
	}
	
	public MinecraftHookBot getBot() {
		return bot;
	}
	
	public void addCommand(Command command) {
		minecrafthook.addCommand(command);
		mch.addCommand(command);
	}
	
	public Config getMCHConfig() {
		return config;
	}
	
	public void onplace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		Bukkit.getLogger().info("1");
		if (item.getItemMeta().getDisplayName().equals("§7Cobblestone §c§nGenbuckets")) {
			Bukkit.getLogger().info("2");
			new BukkitRunnable() {
				Location location = e.getBlockPlaced().getLocation();
				@Override
				public void run() {
					Bukkit.getLogger().info("3");
					if (location.getBlockY() == 256) {
						cancel();
					}
					Bukkit.getLogger().info("4");
					if (location.getBlock().getType() == Material.AIR) {
						return;
					}
					Bukkit.getLogger().info("5");
					location.add(0, 1, 0);
					Bukkit.getLogger().info("6");
					location.getBlock().setType(Material.COBBLESTONE);
					Bukkit.getLogger().info("7");
				}
			}.runTaskTimer(this, 0, 20);
		}
	}
}