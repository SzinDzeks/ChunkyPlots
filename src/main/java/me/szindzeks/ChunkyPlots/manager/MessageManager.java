package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {
	private static ConfigManager configManager = ChunkyPlots.plugin.configManager;
	public static String fixColors(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	public static List<String> fixColors(List<String> list){
		ArrayList<String> fixed = new ArrayList<>();
		for(String string: list)
			fixed.add(ChatColor.translateAlternateColorCodes('&', string));
		return fixed;
	}
	public static void sendNoPrefixMessage(CommandSender sender, String message){
		sender.sendMessage(fixColors(message));
	}
	public static void sendMessage(CommandSender sender, String message){
		sendNoPrefixMessage(sender, configManager.getPluginPrefix() + configManager.getPrefixSpacer() + message);
	}

	public static String replacePlaceholders(String message, VisitPoint visitPoint){
		message = message.replace("{visitPointName}", visitPoint.getName());
		return message;
	}
	public static String replacePlaceholders(String message, Group group){
		message = message.replace("{groupName}", group.getName());
		return message;
	}
	public static String replacePlaceholders(String message, Player player){
		message = message.replace("{user}", player.getName());
		return message;
	}
	public static String replacePlaceholders(String message, User user){
		message = message.replace("{user}", user.getNickname());
		return message;
	}
	public static String replacePlaceholders(String message, Plot plot){
		message = message.replace("{plotID}", plot.getID());
		message = message.replace("{world}", plot.getWorldName());
		return message;
	}
	public static String replacePlaceholders(String message, Plot plot, Flag flag){
		message = replacePlaceholders(message, plot);
		message = message.replace("{flagName}", flag.name());
		message = message.replace("{flagValue}", plot.getFlags().get(flag).toString());
		return message;
	}

	public static String replacePlaceholders(String message, Flag flag, Boolean flagValue){
		message = message.replace("{flagName}", flag.name());
		message = message.replace("{flagValue}", flagValue.toString());
		return message;
	}

	public static String replacePlaceholders(String message, Group group, Flag flag){
		message = replacePlaceholders(message, group);
		message = message.replace("{flagName}", flag.name());
		message = message.replace("{flagValue}", ChunkyPlots.plugin.plotManager.getPlotByUUID(group.plots.get(0)).getFlags().get(flag).toString());
		return message;
	}
	public static String replacePlaceholders(String message, Plot plot, Group group){
		message = replacePlaceholders(message, plot);
		message = replacePlaceholders(message, group);
		return message;
	}
	public static String replacePlaceholders(String message, Plot plot, Player player){
		message = replacePlaceholders(message, plot);
		message = replacePlaceholders(message, player);
		return message;
	}
	public static String replacePlaceholders(String message, Plot plot, User user){
		message = replacePlaceholders(message, plot);
		message = replacePlaceholders(message, user);
		return message;
	}
	public static String replacePlaceholders(String message, VisitPoint visitPoint, Player player){
		message = replacePlaceholders(message, player);
		message = replacePlaceholders(message, visitPoint);
		return message;
	}

	public static String replacePlaceholders(String message, Flag flag) {
		message = message.replace("{flagName}", flag.name());
		return message;
	}
}
