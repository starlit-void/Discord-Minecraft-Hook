package com.pzg.www.api.commands;

import org.bukkit.command.CommandSender;

public interface CommandMethod {
	public boolean run(CommandSender sender, String[] args);
}