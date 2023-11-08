package me.szindzeks.ChunkyPlots.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public abstract class CommandManager implements CommandExecutor {
	private final Command command;
	private final ArrayList<Subcommand> subcommands;
	private String[] subcommandNames;

	public CommandManager(){
		subcommands = getSubcommands();
		setupSubcommandNameArray();
		command = getCommand();
	}

	private void setupSubcommandNameArray() {
		subcommandNames = new String[subcommands.size()];
		for (int i = 0; i < subcommands.size(); i++){
			subcommandNames[i] = subcommands.get(i).getName();
		}
	}

	protected abstract ArrayList<Subcommand> getSubcommands();

	protected abstract Command getCommand();

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
		if(args.length > 0){
			String providedSubcommandName = args[0];
			for (String subcommandName:subcommandNames){
				if(subcommandName.equals(providedSubcommandName)){
					for(Subcommand subcommand:subcommands){
						if(subcommand.getName().equals(subcommandName)){
							subcommand.execute(sender, args);
							return true;
						}
					}
					return true;
				}
			}
		}

		command.execute(sender);
		return true;
	}
}
