package com.pzg.www.minecrafthook.listeners;

import java.util.Calendar;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.pzg.www.minecrafthook.main.PluginMain;

public class ChatListener implements Listener {
	
	private String channel;
	private String format;
	private boolean embed;
	
	public ChatListener(String channel, String format, boolean embed) {
		this.channel = channel;
		this.format = format;
		this.embed = embed;
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void chat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		for (IGuild guild : PluginMain.getInstance().getBot().getGuilds()) {
			for (IChannel channel : guild.getChannels()) {
				if (channel.getName().equalsIgnoreCase(this.channel)) {
					Calendar cal = Calendar.getInstance();
					if (embed) {
						EmbedBuilder eb = new EmbedBuilder();
						
						eb.withTitle("__[**" + player.getName() + "**]__");
						
						eb.withColor(2, 189, 16);
						
						eb.withThumbnail("https://visage.surgeplay.com/head/208/" + player.getUniqueId().toString().replace("-", ""));
						
						eb.appendField("__Said__:", event.getMessage(), true);
						
						String minute = "" + cal.get(Calendar.MINUTE);
						if (minute.length() == 1)
							minute = "0" + minute;
						
						eb.withFooterText(cal.get(Calendar.HOUR) + ":" + minute + " " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR));
						
						channel.sendMessage(eb.build());
					} else {
						String minute = "" + cal.get(Calendar.MINUTE);
						if (minute.length() == 1)
							minute = "0" + minute;
						
						String message = format.replace("{PLAYER}", player.getDisplayName()).replace("{MESSAGE}", event.getMessage()).replace("{DATE}", cal.get(Calendar.HOUR) + ":" + minute + " " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)).replace("&", "�");
						
						channel.sendMessage(message);
					}
				}
			}
		}
	}
}
