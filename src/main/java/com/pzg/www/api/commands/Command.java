package com.pzg.www.api.commands;

import org.bukkit.command.CommandSender;

public class Command {
	
	private String command;
	private String permissions;
	private String noPerms;
	private CommandMethod cmethod;
	
	public Command(String command, String permissions, String noPerms, CommandMethod cmethod) {
		this.command = command;
		this.permissions = permissions;
		this.noPerms = noPerms;
		this.cmethod = cmethod;
	}
	
	public boolean runCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase(this.command)) {
				if (permissions.equals("")) {
					return cmethod.run(sender, args);
				} else if (sender.hasPermission(permissions)) {
					return cmethod.run(sender, args);
				} else sender.sendMessage(noPerms);
			}
		}
		return false;
	}
}
