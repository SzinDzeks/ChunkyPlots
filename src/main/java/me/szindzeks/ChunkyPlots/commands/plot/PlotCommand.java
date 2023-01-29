package me.szindzeks.ChunkyPlots.commands.plot;

import me.szindzeks.ChunkyPlots.commands.Command;
import me.szindzeks.ChunkyPlots.commands.plot.subcommands.PlotHelpCommand;
import org.bukkit.command.CommandSender;

public class PlotCommand extends Command {
	@Override
	public void execute(CommandSender sender) {
		PlotHelpCommand.sendHelpMessage(sender);
	}
}
