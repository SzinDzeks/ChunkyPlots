package me.szindzeks.ChunkyPlots.commands.plot;

import me.szindzeks.ChunkyPlots.commands.Command;
import me.szindzeks.ChunkyPlots.commands.CommandManager;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.commands.plot.subcommands.*;

import java.util.ArrayList;

public class PlotCommandManager extends CommandManager {

	protected ArrayList<Subcommand> getSubcommands() {
		ArrayList<Subcommand> subcommands = new ArrayList<>();
		subcommands.add(new PlotDisposeCommand());
		subcommands.add(new PlotBlacklistCommand());
		subcommands.add(new PlotFlagCommand());
		subcommands.add(new PlotGroupCommand());
		subcommands.add(new PlotHelpCommand());
		subcommands.add(new PlotInfoCommand());
		subcommands.add(new PlotMembersCommand());
		subcommands.add(new PlotVisitCommand());
		subcommands.add(new PlotListCommand());
		return subcommands;
	}

	@Override
	protected Command getCommand() {
		return new PlotCommand();
	}

}
