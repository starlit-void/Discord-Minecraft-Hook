package com.pzg.www.minecrafthook.main;

import com.pzg.www.api.commands.Command;
import com.pzg.www.api.commands.CommandManager;
import com.pzg.www.api.commands.CommandMethod;
import com.pzg.www.api.config.Config;
import com.pzg.www.api.config.ConfigCreate;
import com.pzg.www.minecrafthook.listeners.ChatListener;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
            config.getConfig().set("Discord.Chat.Message.Format", "&6Discord | &b#{CHANNEL} �e{USER} �7: �f{MESSAGE}");

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
                    minecrafthook.addCommand(new Command("verify", "minecrafthook.verify", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "�"), new CommandMethod() {
                        @Override
                        public boolean run(CommandSender sender, String[] args) {
                            if (sender instanceof Player) {
                                if (bot.users.getUser(((Player) sender).getUniqueId()) != null) {
                                    sender.sendMessage(new String[]{"�7--------------------",
                                            "�cYou're already verified!",
                                            "�6Account: " + bot.client.getUserById(bot.users.getUser(((Player) sender).getUniqueId()).getDiscordID()).block().getUsername(),
                                            "�7--------------------"});
                                } else {
                                    if (bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()) == null)
                                        bot.users.addUser(((Player) sender).getUniqueId());
                                    sender.sendMessage(new String[]{"�7--------------------",
                                            "�b Please type the command in discord:",
                                            "�6  - '!verify " + bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()).getRandom() + "'",
                                            "�7--------------------"});
                                }

                                return true;
                            } else
                                sender.sendMessage("�cUh oh, this is a player only command!");
                            return false;
                        }
                    }));
                    mch.addCommand(new Command("verify", "minecrafthook.verify", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "�"), new CommandMethod() {
                        @Override
                        public boolean run(CommandSender sender, String[] args) {
                            if (sender instanceof Player) {
                                if (bot.users.getUser(((Player) sender).getUniqueId()) != null) {
                                    System.out.println("User found.");
                                    sender.sendMessage(new String[]{"�7--------------------",
                                            "�cYou're already verified!",
                                            "�6Account: " + bot.client.getUserById(bot.users.getUser(((Player) sender).getUniqueId()).getDiscordID()).block().getUsername(),
                                            "�7--------------------"});
                                    return true;
                                } else {
                                    if (bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()) == null)
                                        bot.users.addUser(((Player) sender).getUniqueId());
                                    sender.sendMessage(new String[]{"�7--------------------",
                                            "�b Please type the command in discord:",
                                            "�6  - '!verify " + bot.users.getUserAwaitingConf(((Player) sender).getUniqueId()).getRandom() + "'",
                                            "�7--------------------"});
                                    return true;
                                }
                            } else
                                sender.sendMessage("�cUh oh, this is a player only command!");
                            return false;
                        }
                    }));
                }

                if (config.getConfig().getBoolean("Minecraft.Command.Help")) {
                    minecrafthook.addCommand(new Command("help", "minecrafthook.help", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "�"), (sender, args) -> {
                        sender.sendMessage(new String[]{"�7--------------------",
                                "�6Commands:",
                                "�6    + �7/minecrafthook verify",
                                "�6      - Verify your discord account.",
                                "�6      - Alt, �7/mch verify",
                                "�6    + �7/minecrafthook help",
                                "�6      - Send a help message.",
                                "�6      - Alt, �7/mch help",
                                "�7--------------------"});
                        return true;
                    }));
                    mch.addCommand(new Command("help", "minecrafthook.help", config.getConfig().getString("Minecraft.Command.Message.Error").replace("&", "�"), (sender, args) -> {
                        sender.sendMessage(new String[]{"�7--------------------",
                                "�6Commands:",
                                "�6    + �7/minecrafthook verify",
                                "�6      - Verify your discord account.",
                                "�6      - Alt, �7/mch verify",
                                "�6    + �7/minecrafthook help",
                                "�6      - Send a help message.",
                                "�6      - Alt, �7/mch help",
                                "�7--------------------"});
                        return true;
                    }));
                }

                if (config.getConfig().getBoolean("Minecraft.Chat.Sync")) {
                    Bukkit.getPluginManager().registerEvents(new ChatListener(config.getConfig().getString("Minecraft.Chat.Channel"), config.getConfig().getString("Minecraft.Chat.Message.Format"), config.getConfig().getBoolean("Minecraft.Chat.Message.Embed")), plugin);
                }

                if (config.getConfig().getBoolean("Discord.Chat.Sync")) {
                    bot.client.getEventDispatcher()
                            .on(ReadyEvent.class)
                            .map(event -> event.getGuilds().size())
                            .flatMap(size -> bot.client.getEventDispatcher()
                                    .on(GuildCreateEvent.class)
                                    .take(size)
                                    .last()).subscribe(event -> {
                        System.out.println("Finished loading guilds.");

                        List<String> channelsToWatch = config.getConfig().getStringList("Discord.Chat.Channels");

                        bot.client.getGuilds()
                                .flatMap(Guild::getChannels)
                                .filter(chan -> channelsToWatch.contains(chan.getName()))
                                .subscribe(channel -> {
                                    if (channel instanceof MessageChannel) {
                                        MessageChannel msgChannel = (MessageChannel) channel;

                                        msgChannel.getMessagesAfter(Snowflake.of(Instant.now())).subscribe(msg -> {

                                            String userName = msg.getAuthor().map(User::getUsername).orElse("<unknown>");
                                            String message = msg.getContent().orElse("<no content>");
                                            Bukkit.broadcastMessage(config.getConfig().getString("Discord.Chat.Message.Format").replace("{CHANNEL}", channel.getName()).replace("{USER}", userName).replace("{MESSAGE}", message).replace("&", "§"));
                                        });
                                    }
                                });
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
        if (item.getItemMeta().getDisplayName().equals("�7Cobblestone �c�nGenbuckets")) {
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
