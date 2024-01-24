package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.basic.VisitPoint;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils {
	public static String fixColors(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	public static List<String> fixColors(List<String> listString){
		List<String> fixedStringList = new ArrayList<>();
		for(String string:listString){
			fixedStringList.add(fixColors(string));
		}
		return fixedStringList;
	}

	public static void sendMessage(CommandSender sender, String message){
		sendNoPrefixMessage(sender, ChunkyPlots.plugin.configManager.getPluginPrefix() + message);
	}

	public static void sendNoPrefixMessage(CommandSender sender, String message){
		sender.sendMessage(fixColors(message));
	}

}
