package me.szindzeks.ChunkyPlots.commands;

import me.szindzeks.ChunkyPlots.manager.MessageManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandManager implements CommandExecutor {
	private final Command command;
	private final ArrayList<Subcommand> subcommands;

	public CommandManager(){
		subcommands = getSubcommands();
		command = getCommand();
	}

	protected abstract ArrayList<Subcommand> getSubcommands();

	protected abstract Command getCommand();

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
		if(args.length > 0){
			Subcommand subcommand = getSubcommand(args[0]);
			if(subcommand != null)
				subcommand.execute(sender, args);
            else
				MessageManager.sendMessage(sender, "&cCouldn't find matching subcommand to execute.");
		} else command.execute(sender);

		return true;
	}

	private Subcommand getSubcommand(String providedSubcommandName) {
		List<Subcommand> matchingSubcommands = getMatchingSubcommands(providedSubcommandName);
		if(matchingSubcommands.size() == 1)
			return matchingSubcommands.get(0);
		else
			return null;
	}

	private List<Subcommand> getMatchingSubcommands(String providedSubcommandName) {
		return subcommands.stream().filter(sub -> sub.getName().equals(providedSubcommandName)).toList();
	}
}
