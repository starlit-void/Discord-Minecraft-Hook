package com.pzg.www.minecrafthook.listeners;

import com.pzg.www.minecrafthook.main.PluginMain;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.MessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.util.Calendar;
import java.util.List;

public class ChatListener implements Listener {

    private String channel;
    private String format;
    private boolean embed;

    public ChatListener(String channel, String format, boolean embed) {
        this.channel = channel;
        this.format = format;
        this.embed = embed;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        List<Guild> guilds = PluginMain.getInstance().getBot().getGuilds();
        if (guilds == null) {
            return;
        }

        for (Guild guild : guilds) {
            List<GuildChannel> channels = guild.getChannels().collectList().block();
            if (channels == null) {
                continue;
            }

            for (GuildChannel channel : channels) {

                MessageChannel messageChannel;
                if (channel instanceof MessageChannel) {
                    messageChannel = (MessageChannel) channel;
                } else {
                    continue;
                }

                if (channel.getName().equalsIgnoreCase(this.channel)) {
                    Calendar cal = Calendar.getInstance();
                    if (embed) {
                        messageChannel.createEmbed(embedCreate -> {
                            embedCreate.setTitle("__[**" + player.getName() + "**]__");

                            embedCreate.setColor(new Color(2, 189, 16));

                            embedCreate.setThumbnail("https://visage.surgeplay.com/head/208/" + player.getUniqueId().toString().replace("-", ""));

                            embedCreate.addField("__Said__:", event.getMessage(), true);

                            String minute = "" + cal.get(Calendar.MINUTE);
                            if (minute.length() == 1)
                                minute = "0" + minute;

                            embedCreate.setFooter(cal.get(Calendar.HOUR) + ":" + minute + " " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR), null);
                        });
                    } else {
                        String minute = "" + cal.get(Calendar.MINUTE);
                        if (minute.length() == 1)
                            minute = "0" + minute;

                        String message = format.replace("{PLAYER}", player.getDisplayName()).replace("{MESSAGE}", event.getMessage()).replace("{DATE}", cal.get(Calendar.HOUR) + ":" + minute + " " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)).replace("&", "�");

                        messageChannel.createMessage(message);
                    }
                }
            }
        }
    }
}
