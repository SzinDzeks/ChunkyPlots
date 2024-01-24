package me.szindzeks.ChunkyPlots.commands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import me.szindzeks.ChunkyPlots.manager.VisitManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Subcommand{

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getSyntax();

	public abstract String getPermission();

	public abstract void execute(CommandSender sender, String[] args);
}
