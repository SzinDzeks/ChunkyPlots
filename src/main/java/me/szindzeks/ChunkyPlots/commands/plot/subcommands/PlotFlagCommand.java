package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.commands.plot.PlotCommandManager;
import me.szindzeks.ChunkyPlots.manager.*;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static me.szindzeks.ChunkyPlots.commands.plot.subcommands.PlotGroupCommand.getPlotsFromGroupName;

public class PlotFlagCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	
	@Override
	public String getName() {
		return "flag";
	}

	@Override
	public String getDescription() {
		return "zarządzanie flagami działki";
	}

	@Override
	public String getSyntax() {
		return "/plot flag";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.flag";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player player){
			if(args.length == 2) {
				if (args[1].equals("defaults")) displayFlags(player, true, null);
				else if (args[1].equals("list"))
					displayFlags(player, false, plotManager.getPlotByChunk(player.getLocation().getChunk()));
				else PlotCommandManager.sendHelpMessage(player);
			} else if(args.length == 3){
				if(args[1].equals("check")) displayFlagValue(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
			} else if(args.length == 4){
//				TODO: Fix concurrent modification exception
				if (args[1].equals("check")) {
					List<Plot> plots = getPlotsFromGroupName(player, args[3]);
					if(plots.size() > 0) {
						boolean isValueSameInAllPlots = true;
						Flag flag = Flag.getByName(args[2]);
						if(flag != null) {
							boolean value = plots.get(0).getFlags().get(flag);
							for (Plot plot : plots) {
								if(!value == plot.getFlags().get(flag)){
									isValueSameInAllPlots = false;
									break;
								}
							}
							if(isValueSameInAllPlots){
								String rawMessage = configManager.getMessage(MessageType.FLAG_VALUE_ON_PLOT);
								String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, flag, value);
								MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
							} else {
								String rawMessage = configManager.getMessage(MessageType.FLAG_VALUES_IN_GROUP_ARE_DIFFERENT);
								String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, flag);
								MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
							}
						}
					}
					else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
				}
				else if (args[1].equals("set")) setFlagValue(player, args[2], args[3], plotManager.getPlotByChunk(player.getLocation().getChunk()));
			} else if(args.length == 5){
				if (args[1].equals("set")){
					List<Plot> plots = getPlotsFromGroupName(player, args[4]);
					if(plots.size() > 0) for(Plot plot: plots) setFlagValue(player, args[2], args[3], plot);
					else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
				}
				else if(args[1].equals("list")) displayFlags(player, false, plotManager.getPlotByCoordinates(args[2], args[3], args[4]));
//				TODO: flag help message
			}
		}
	}

	private void displayFlagValue(Player player, String flagName, Plot plot) {
		if(plot != null){
//			TODO: message manager flag value
			Flag flagToDisplay = Flag.getByName(flagName);
			if(flagToDisplay != null){
				String flagValue = plot.getFlags().get(flagToDisplay).toString();

				String rawMessage = configManager.getMessage(MessageType.FLAG_VALUE_ON_PLOT);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, flagToDisplay);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = configManager.getMessage(MessageType.UNKNOWN_FLAG);
				String uncolouredMessage = rawMessage.replace("{flagName}", flagName.toUpperCase());
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}



	private void displayFlags(Player player, boolean defaultOrNot, Plot plot) {
		HashMap<Flag, Boolean> flags;
		if(defaultOrNot) {
			flags = configManager.getDefaultFlags();
			player.sendMessage(ChatUtils.fixColors("&a&lLista domyślnych flag dla działek:"));
		} else {
			if(plot != null){
				flags = plot.getFlags();
				player.sendMessage(ChatUtils.fixColors("&aLista flag dla działki o ID &8(&7" + plot.getID() + "&8)&a:"));
			} else {
				player.sendMessage(configManager.getMessage(MessageType.NULL_PLOT));
				return;
			}
		}
		if (flags != null) {
			for(Flag flag:flags.keySet()){
				boolean flagValue = flags.get(flag);
				player.sendMessage(ChatUtils.fixColors("&7" + flag.toString() + "&8: " + (flagValue ? ("&a" + flagValue) : ("&c" + flagValue))));
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.ERROR_UNSPECIFIED);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void setFlagValue(Player player, String flagName, String flagValue, Plot plot) {
		if(plot != null){
			if(plot.getOwnerNickname().equals(player.getName())){
				Flag flag = Flag.valueOf(flagName.toUpperCase());
//				TODO: Proper checking if flag exists before taking value of it
				if(flag != null){
					if(flagValue.equalsIgnoreCase("true") || flagValue.equalsIgnoreCase("false")){
						boolean value = Boolean.parseBoolean(flagValue);
						plot.setFlag(flag, value);
						plotManager.savePlot(plot);

						String rawMessage = configManager.getMessage(MessageType.FLAG_SET_ON_PLOT);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, flag);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					} else {
						String rawMessage = configManager.getMessage(MessageType.WRONG_FLAG_VALUE);
						String uncolouredMessage = rawMessage.replace("{flagValue}", flagValue.toUpperCase());
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.UNKNOWN_FLAG);
					String uncolouredMessage = rawMessage.replace("{flagName}", flagName.toUpperCase());
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = configManager.getMessage(MessageType.NOT_OWNER);
				MessageManager.sendColouredMessageToPlayer(rawMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}
}
