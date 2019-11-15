package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.basic.VisitPoint;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
	public static String fixColors(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	public static String replacePlaceholders(String txt, Player player, Plot plot, User user, Flag flag, String value){
		if((txt.contains("{player}") || txt.contains("{user}")) && user != null && user.getNickname() != null) {
			txt.replace("{player}", user.getNickname());
			txt.replace("{user}", user.getNickname());
		}
		if(txt.contains("{plotID}") || txt.contains("{id}") && plot != null && plot.getID() != null)
			txt.replace("{plotID}", plot.getID());
			txt.replace("{id}", plot.getID());
		if(txt.contains("{flag}") && flag != null)
			txt.replace("{flag}", flag.name());
		if(txt.contains("{value}") && value != null)
			txt.replace("{value}", value);
		if(txt.contains("{world}") && plot.getWorldName() != null)
			txt.replace("{world}", plot.getWorldName());
		if(txt.contains("{plotOwner}") && plot != null && plot.getOwnerNickname() != null)
			txt.replace("{plotOwner}", plot.getOwnerNickname());
		return txt;
	}
	public static String replacePlaceholders(String txt, VisitPoint visitPoint){
		if(txt.contains("{visitPointName}") && visitPoint.getName() != null)
				txt.replace("{visitPointName}", visitPoint.getName());
		return txt;
	}
}
