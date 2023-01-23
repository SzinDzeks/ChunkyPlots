package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import org.bukkit.command.CommandSender;

public class PlotHelpCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Wyświetla listę komend plugin";
	}

	@Override
	public String getSyntax() {
		return "/plot help";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sendHelpMessage(sender);
	}

	public static void sendHelpMessage(CommandSender sender){
		sender.sendMessage(ChatUtils.fixColors("&9-----------{ " + ChunkyPlots.plugin.configManager.getPluginPrefix() + " &9}-----------"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot help "));
		sender.sendMessage(ChatUtils.fixColors("&9========================================"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot dispose "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot list "));
		sender.sendMessage(ChatUtils.fixColors("&9========================================"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot info "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot info <x działki> <z działki> <nazwa świata>"));
		sender.sendMessage(ChatUtils.fixColors("&9========================================"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot members add <nick> "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot members add <nick> <x działki> <z działki> <nazwa świata> "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot members remove <nick>"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot members remove <nick> <x działki> <z działki> <nazwa świata> "));
		sender.sendMessage(ChatUtils.fixColors("&9========================================"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group create <nazwa grupy> "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group delete <nazwa grupy> "));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group add <nazwa grupy>"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group add <nazwa grupy> <x działki> <z działki> <nazwa świata>"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group remove <nazwa grupy>"));
		sender.sendMessage(ChatUtils.fixColors("&a/plot group remove <nazwa grupy> <x działki> <z działki> <nazwa świata>"));
		sender.sendMessage(ChatUtils.fixColors("&9-----------{ " + ChunkyPlots.plugin.configManager.getPluginPrefix() + " &9}-----------"));
	}
}
