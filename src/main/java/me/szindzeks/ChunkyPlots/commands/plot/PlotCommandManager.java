package me.szindzeks.ChunkyPlots.commands.plot;

import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.commands.plot.subcommands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.logging.Level;

public class PlotCommandManager implements CommandExecutor {
	private ArrayList<Subcommand> subcommands = new ArrayList<>();
	private String[] subcommandNames;

	public PlotCommandManager(){
		setupSubcommands();
		setupSubcommandNameArray();
	}

	private void setupSubcommandNameArray() {
		subcommandNames = new String[subcommands.size()];
		for (int i = 0; i < subcommands.size(); i++){
			subcommandNames[i] = subcommands.get(i).getName();
		}
	}

	private void setupSubcommands() {
		subcommands.add(new PlotDisposeCommand());
		subcommands.add(new PlotBlacklistCommand());
		subcommands.add(new PlotFlagCommand());
		subcommands.add(new PlotGroupCommand());
		subcommands.add(new PlotGuiCommand());
		subcommands.add(new PlotHelpCommand());
		subcommands.add(new PlotInfoCommand());
		subcommands.add(new PlotMembersCommand());
		subcommands.add(new PlotVisitCommand());
		subcommands.add(new PlotListCommand());
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		double start = System.nanoTime();
		if(args.length > 0){
			String providedSubcommandName = args[0];
			for (String subcommandName:subcommandNames){
				if(subcommandName.equals(providedSubcommandName)){
					for(Subcommand subcommand:subcommands){
						if(subcommand.getName().equals(subcommandName)){
							subcommand.execute(sender, args);
							sender.sendMessage("Execution time: "+(System.nanoTime()-start)/1000000+"ms");
							return true;
						}
					}
					return true;
				}
			}
		}
		PlotHelpCommand.sendHelpMessage(sender);
		double timeElapsed = (System.nanoTime()-start)/1000000;
		sender.sendMessage("Execution time: "+timeElapsed+"ms");
		return true;
	}
}
