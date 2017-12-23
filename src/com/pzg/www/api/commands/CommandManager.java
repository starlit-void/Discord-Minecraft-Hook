package com.pzg.www.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
	
	private String label;
	List<Command> commands = new ArrayList<Command>();
	
	public CommandManager(String label) {
		this.label = label;
	}
	
	
	public void addCommand(Command command) {
		commands.add(command);
	}
	
	public Command getCommand(int index) {
		return commands.get(index);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command paramCommand, String label, String[] args) {
		if (label.equalsIgnoreCase(this.label)) {
			boolean returnB = false;
			for (Command command : commands) {
				returnB = command.runCommand(sender, command, label, args);
			}
			return returnB;
		}
		return false;
	}
}