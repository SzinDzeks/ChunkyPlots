package me.szindzeks.ChunkyPlots.commands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.*;
import me.szindzeks.ChunkyPlots.guis.MenuGUI;
import me.szindzeks.ChunkyPlots.manager.*;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;

public class PlotCommand implements CommandExecutor {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;
	final VisitManager visitManager = ChunkyPlots.plugin.visitManager;
	final HashMap<MessageType, String> messages = configManager.getMessages();

	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 1){
				if(args[0].equals("dispose")) disposePlot(player);
				else if(args[0].equals("info")) displayPlotInfo(player, null);
				else if(args[0].equals("list")) displayPlotList(player);
				else if(args[0].equals("gui")) player.openInventory(new MenuGUI(null).getInventory());
				else displayHelpMessage(player);
			} else if(args.length == 2){
				if(args[0].equals("info")){
					List<Plot> plots = getPlotsFromGroupName(player, args[1]);
					if(plots.size() > 0) for(Plot plot: plots) displayPlotInfo(player, plot);
					else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
				}
				else if(args[0].equals("flag") && player.hasPermission("chunkyplots.flag")){
					if(args[1].equals("defaults")) displayFlags(player, true, null);
					else if(args[1].equals("list")) displayFlags(player, false, plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				}
				else if(args[0].equals("visit")) visitPlot(player, args[1]);
				else displayHelpMessage(player);
			} else if(args.length == 3){
				if(args[0].equals("members")){
					if(args[1].equals("add")) addMemberToPlot(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else if(args[1].equals("remove")) removeMemberFromPlot(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				}
				else if(args[0].equals("blacklist") && player.hasPermission("chunkyplots.blacklist")){
					if(args[1].equals("add")) addPlayerToBlacklist(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else if(args[1].equals("remove")) removePlayerFromBlacklist(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				}
				else if(args[0].equals("flag") && player.hasPermission("chunkyplots.flag")){
					if(args[1].equals("check")) displayFlagValue(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				}
				else if(args[0].equals("group")){
					if(args[1].equals("create")) createGroup(player, args[2]);
					else if(args[1].equals("delete")) deleteGroup(player, args[2]);
					else if(args[1].equals("add")) addPlotToGroup(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else if(args[1].equals("remove")) removePlotFromGroup(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				}
				else if(args[0].equals("visit")){
					if(args[1].equals("createpoint")) createVisitPoint(player, args[2], null, plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else if(args[1].equals("deletepoint")) deleteVisitPoint(player, args[2]);
					else displayHelpMessage(player);
				}
				else displayHelpMessage(player);
			} else if(args.length == 4) {
				if (args[0].equals("members")) {
					if (args[1].equals("add")) {
						List<Plot> plots = getPlotsFromGroupName(player, args[3]);
						if(plots.size() > 0) for (Plot plot : plots) addMemberToPlot(player, args[2], plot);
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else if (args[1].equals("remove")) {
						List<Plot> plots = getPlotsFromGroupName(player, args[3]);
						if(plots.size() > 0) for (Plot plot : plots) removeMemberFromPlot(player, args[2], plot);
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else displayHelpMessage(player);
				} else if (args[0].equals("blacklist") && player.hasPermission("chunkyplots.blacklist")) {
					if (args[1].equals("add")) {
						List<Plot> plots = getPlotsFromGroupName(player, args[3]);
						if(plots.size() > 0) for(Plot plot: plots) addPlayerToBlacklist(player, args[2], plot );
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else if (args[1].equals("remove")) {
						List<Plot> plots = getPlotsFromGroupName(player, args[3]);
						if(plots.size() > 0) for(Plot plot: plots) removePlayerFromBlacklist(player, args[2], plot);
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else displayHelpMessage(player);
				} else if (args[0].equals("flag") && player.hasPermission("chunkyplots.flag")) {
					if (args[1].equals("check")) {
						List<Plot> plots = getPlotsFromGroupName(player, args[3]);
						if(plots.size() > 0) for(Plot plot: plots) displayFlagValue(player, args[2], plot);
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else if (args[1].equals("set")) setFlagValue(player, args[2], args[3], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else displayHelpMessage(player);
				} else displayHelpMessage(player);
			} else if(args.length == 5){
				if(args[0].equals("flag") && player.hasPermission("chunkyplots.flag")) {
					if (args[1].equals("set")){
						List<Plot> plots = getPlotsFromGroupName(player, args[4]);
						if(plots.size() > 0) for(Plot plot: plots) setFlagValue(player, args[2], args[3], plot);
						else player.sendMessage(ChatUtils.fixColors("&cDo tej grupy nie są przypisane żadne działki"));
					}
					else if(args[1].equals("list")) displayFlags(player, false, plotManager.getPlotByCoordinates(args[2], args[3], args[4]));
					else displayHelpMessage(player);
				} else displayHelpMessage(player);
			} else if(args.length == 6){
				if (args[0].equals("members")) {
					if (args[1].equals("add")) addMemberToPlot(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else if (args[1].equals("remove"))removeMemberFromPlot(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else displayHelpMessage(player);
				} else if (args[0].equals("blacklist") && player.hasPermission("chunkyplots.blacklist")) {
					if (args[1].equals("add")) addPlayerToBlacklist(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else if (args[1].equals("remove")) removePlayerFromBlacklist(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else displayHelpMessage(player);
				} else if(args[0].equals("group")) {
					if (args[1].equals("add")) addPlotToGroup(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else if (args[1].equals("remove")) removePlotFromGroup(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
					else displayHelpMessage(player);
				}
				else displayHelpMessage(player);
			} else if(args.length == 7){
				if(args[0].equals("flag") && player.hasPermission("chunkyplots.flag")){
					if(args[1].equals("set")) setFlagValue(player, args[2], args[3], plotManager.getPlotByCoordinates(args[4], args[5], args[6]));
					else displayHelpMessage(player);
				} else displayHelpMessage(player);
			} else displayHelpMessage(player);
		} else sender.sendMessage(configManager.getMessages().get(MessageType.SENDER_NOT_PLAYER));
		return true;
	}

	private void displayHelpMessage(Player player){
		player.sendMessage(ChatUtils.fixColors("&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------"));
		player.sendMessage(ChatUtils.fixColors("&a/plot help "));
		player.sendMessage(ChatUtils.fixColors("&9========================================"));
		player.sendMessage(ChatUtils.fixColors("&a/plot dispose "));
		player.sendMessage(ChatUtils.fixColors("&a/plot list "));
		player.sendMessage(ChatUtils.fixColors("&9========================================"));
		player.sendMessage(ChatUtils.fixColors("&a/plot info "));
		player.sendMessage(ChatUtils.fixColors("&a/plot info <x działki> <z działki> <nazwa świata>"));
		player.sendMessage(ChatUtils.fixColors("&9========================================"));
		player.sendMessage(ChatUtils.fixColors("&a/plot members add <nick> "));
		player.sendMessage(ChatUtils.fixColors("&a/plot members add <nick> <x działki> <z działki> <nazwa świata> "));
		player.sendMessage(ChatUtils.fixColors("&a/plot members remove <nick>"));
		player.sendMessage(ChatUtils.fixColors("&a/plot members remove <nick> <x działki> <z działki> <nazwa świata> "));
		player.sendMessage(ChatUtils.fixColors("&9========================================"));
		player.sendMessage(ChatUtils.fixColors("&a/plot group create <nazwa grupy> "));
		player.sendMessage(ChatUtils.fixColors("&a/plot group delete <nazwa grupy> "));
		player.sendMessage(ChatUtils.fixColors("&a/plot group add <nazwa grupy>"));
		player.sendMessage(ChatUtils.fixColors("&a/plot group add <nazwa grupy> <x działki> <z działki> <nazwa świata>"));
		player.sendMessage(ChatUtils.fixColors("&a/plot group remove <nazwa grupy>"));
		player.sendMessage(ChatUtils.fixColors("&a/plot group remove <nazwa grupy> <x działki> <z działki> <nazwa świata>"));
		player.sendMessage(ChatUtils.fixColors("&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------"));
	}

	private void disposePlot(Player player){
		Chunk chunk = player.getLocation().getChunk();
		String plotID = chunk.getX() + ";" + chunk.getZ();
		Plot plot = plotManager.getPlotByChunk(chunk);
		if(plot != null){
			if(plot.getOwnerNickname().equals(player.getName())) {
				plotManager.removePlot(plot);
				player.getInventory().addItem(CraftingManager.plotBlock);
				player.sendMessage(messages.get(MessageType.PLOT_DELETED).replace("{plotID}", plotID).replace("{world}", plot.getWorldName()));

				User user = userManager.getUser(player.getName());
				for(Group group:user.groups){
					group.plots.remove(plot.getUUID());
				}

				List<VisitPoint> visitPointsToDelete = new ArrayList<>();
				for (VisitPoint visitPoint : visitManager.getVisitPoints()) {
					if (visitPoint.getPlotUUID().equals(plot.getUUID())) visitPointsToDelete.add(visitPoint);
				}
				for (VisitPoint visitPoint : visitPointsToDelete) {
					visitManager.deleteVisitPoint(visitPoint);
					player.sendMessage(ChatUtils.fixColors("&cNa usuniętej działce znajdował się punkt &f" + visitPoint.getName() + "&c, więc został on usunięty!"));
				}
			} else player.sendMessage(messages.get(MessageType.NOT_OWNER));
		} else player.sendMessage(messages.get(MessageType.NULL_PLOT).replace("{plotID}", plotID));
	}

	private void displayPlotList(Player player){
		player.sendMessage("");
		player.sendMessage("");
		player.sendMessage(ChatUtils.fixColors("&eID Działki &9» &ePrzybliżone koordynaty działki"));
		for(Plot plot:plotManager.getPlots()){
			if(plot.getOwnerNickname().equals(player.getName())) {
				Location location = player.getWorld().getChunkAt(plot.getChunkX(), plot.getChunkZ()).getBlock(8, 64, 8).getLocation();
				player.sendMessage(ChatUtils.fixColors("&8(&6" + plot.getID() + "&8)" + " &a» &7X:&f" + location.getBlockX() + "  &7Y:&f" + location.getBlockY() + "  &7Z:&f" + location.getBlockZ()));
			}
		}
		player.sendMessage("");
		player.sendMessage("");
	}

	private void displayMarketListenings(Player player){

	}
	private void removePlotFromMarket(Player player, Plot plot) {

	}
	private void buyPlotFromMarket(Player player, Plot plot) {

	}
	private void addPlotToMarket(Player player, Plot plot, int price){

	}

	private void displayPlotInfo(Player player, Plot plot){
		if(plot == null) plot = plotManager.getPlotByChunk(player.getLocation().getChunk());
		if (plot != null) {
			player.sendMessage(ChatUtils.fixColors("&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------"));
			player.sendMessage(ChatUtils.fixColors("&aWłaściciel: &f" + plot.getOwnerNickname()));
			player.sendMessage(ChatUtils.fixColors("&aChunk X: &f" + plot.getChunkX()));
			player.sendMessage(ChatUtils.fixColors("&aChunk Z: &f" + plot.getChunkZ()));
			player.sendMessage(ChatUtils.fixColors("&aŚwiat: &f" + plot.getWorldName()));
			player.sendMessage(ChatUtils.fixColors("&aCzłonkowie: &f" + plot.members.toString()));
			player.sendMessage(ChatUtils.fixColors("&aZablokowani: &f" + plot.blacklist.toString()));
			Location location = player.getWorld().getChunkAt(plot.getChunkX(), plot.getChunkZ()).getBlock(8, 64, 8).getLocation();
			player.sendMessage(ChatUtils.fixColors("&aLokalizacja: &7X:&f" + location.getBlockX() + "  &7Y:&f" + location.getBlockY() + "  &7Z:&f" + location.getBlockZ()));
			player.sendMessage(ChatUtils.fixColors("&aUUID: &f" + plot.getUUID()));
			player.sendMessage(ChatUtils.fixColors("&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------"));
		} else player.sendMessage(messages.get(MessageType.NULL_PLOT));
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
				player.sendMessage(messages.get(MessageType.NULL_PLOT));
				return;
			}
		}
		if (flags != null) {
			for(Flag flag:flags.keySet()){
				boolean flagValue = flags.get(flag);
				player.sendMessage(ChatUtils.fixColors("&7" + flag.toString() + "&8: " + (flagValue ? ("&a" + flagValue) : ("&c" + flagValue))));
			}
		} else {
			String rawMessage = messages.get(MessageType.ERROR_UNSPECIFIED);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void displayFlagValue(Player player, String flagName, Plot plot) {
		if(plot != null){
			Flag flag = Flag.valueOf(flagName.toUpperCase());
			if(flag != null){
				String flagValue = plot.getFlags().get(flag).toString();

				String rawMessage = messages.get(MessageType.FLAG_VALUE_ON_PLOT);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, flag);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = messages.get(MessageType.UNKNOWN_FLAG);
				String uncolouredMessage = rawMessage.replace("{flagName}", flagName.toUpperCase());
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void setFlagValue(Player player, String flagName, String flagValue, Plot plot) {
		if(plot != null){
			if(plot.getOwnerNickname().equals(player.getName())){
				Flag flag = Flag.valueOf(flagName.toUpperCase());
				if(flag != null){
					if(flagValue.equalsIgnoreCase("true") || flagValue.equalsIgnoreCase("false")){
						boolean value = Boolean.valueOf(flagValue);
						plot.setFlag(flag, value);
						plotManager.savePlot(plot);

						String rawMessage = messages.get(MessageType.FLAG_SET_ON_PLOT);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, flag);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					} else {
						String rawMessage = messages.get(MessageType.WRONG_FLAG_VALUE);
						String uncolouredMessage = rawMessage.replace("{flagValue}", flagValue.toUpperCase());
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					}
				} else {
					String rawMessage = messages.get(MessageType.UNKNOWN_FLAG);
					String uncolouredMessage = rawMessage.replace("{flagName}", flagName.toUpperCase());
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NOT_OWNER);
				MessageManager.sendColouredMessageToPlayer(rawMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void visitPlot(final Player player, final String visitPointName){
		final VisitPoint visitPoint = visitManager.getVisitPoint(visitPointName);
		if(visitPoint != null) {
			if(visitPoint.isOpen) {
				if(visitPoint.isSafe()) {
					final User user = userManager.getUser(player.getName());
					if (user.isTeleporting == false) {
						user.isTeleporting = true;
						String rawMessage = messages.get(MessageType.TELEPORTING_TO_VISIT_POINT);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
						final int x = player.getLocation().getBlockX();
						final int y = player.getLocation().getBlockY();
						final int z = player.getLocation().getBlockZ();
						ChunkyPlots.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ChunkyPlots.plugin, new Runnable() {
							@Override
							public void run() {
								if (player.isOnline()) {
									if (player.getLocation().getBlockX() == x && player.getLocation().getBlockY() == y && player.getLocation().getBlockZ() == z) {
										player.teleport(visitPoint.getLocation());
										String rawMessage = messages.get(MessageType.TELEPORTED_TO_VISIT_POINT);
										String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
										MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
									} else {
										String rawMessage = messages.get(MessageType.TELEPORT_CANCELLED);
										String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
										MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
									}
								}
								user.isTeleporting = false;
							}
						}, 20 * 5);
					} else {
						String rawMessage = messages.get(MessageType.ALREADY_TELEPORTING);
						MessageManager.sendColouredMessageToPlayer(rawMessage, player);
					}
				} else {
					String rawMessage = messages.get(MessageType.VISIT_POINT_NOT_SAFE);
					MessageManager.sendColouredMessageToPlayer(rawMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.VISIT_POINT_CLOSED);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_VISIT_POINT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void createVisitPoint(Player player, String name, String description, Plot plot){
		if(plot != null) {
			for (VisitPoint visitPoint : visitManager.getVisitPoints()) {
				if (visitPoint.getName().equals(name)) {
					String rawMessage = messages.get(MessageType.VISIT_POINT_ALREADY_EXISTS);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					return;
				}
			}
			Location location = player.getLocation();
			VisitPoint visitPoint = new VisitPoint(location, plot.getUUID(), player.getName(), name, description);
			visitManager.createVisitPoint(visitPoint);

			String rawMessage = messages.get(MessageType.CREATED_VISIT_POINT);
			String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		} else {
			String rawMessage = messages.get(MessageType.VISIT_POINT_NOT_INSIDE_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void deleteVisitPoint(Player player, String visitPointName){
		VisitPoint visitPoint = visitManager.getVisitPoint(visitPointName);
		if(visitPoint != null) {
			if(visitPoint.getOwnerName().equals(player.getName())){
				visitManager.deleteVisitPoint(visitPoint);

				String rawMessage = messages.get(MessageType.DELETED_VISIT_POINT);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = messages.get(MessageType.NOT_VISIT_POINT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_VISIT_POINT);
			String uncolouredMessage = rawMessage.replace("{visitPoint}", visitPointName);
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		}
	}

	private void setVisitPointStatus(Player player, String visitPointName, boolean status){
		VisitPoint visitPoint = visitManager.getVisitPoint(visitPointName);
		if(visitPoint != null) {
			if(visitPoint.getOwnerName().equals(player.getName())){
				visitPoint.isOpen = status;
				if(status == true) player.sendMessage(ChatUtils.fixColors("&7Pomyślnie &awłączono &7punkt odwiedziń o nazwie &f" + visitPointName));
				else player.sendMessage(ChatUtils.fixColors("&7Pomyślnie &cwyłączono &7punkt odwiedziń o nazwie &f" + visitPointName));
			} else player.sendMessage(ChatUtils.fixColors("&cNie jesteś właścicielem punktu o nazwie &f" + visitPointName));
		} else player.sendMessage(ChatUtils.fixColors("&cPunkt o nazwie &f" + visitPointName + " &cnie istnieje!"));
	}


	private void addMemberToPlot(Player player, String userName, Plot plot) {
		if (plot != null) {
			if (plot.getOwnerNickname().equals(player.getName())) {
				if (!userName.equals(plot.getOwnerNickname())) {
					User user = userManager.getUser(userName);
					if (user != null) {
						plot.members.add(userName);
						plotManager.savePlot(plot);
						String rawMessage = messages.get(MessageType.ADDED_MEMBER_TO_PLOT);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, user);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					} else {
						String rawMessage = messages.get(MessageType.NULL_USER);
						MessageManager.sendColouredMessageToPlayer(rawMessage, player);
					}
				} else {
					String rawMessage = messages.get(MessageType.CANNOT_ADD_OWNER_AS_MEMBER);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void removeMemberFromPlot(Player player, String userName, Plot plot){
		if(plot != null) {
			if(plot.getOwnerNickname().equals(player.getName())){
				if(plot.members.contains(userName)){
					plot.members.remove(userName);
					plotManager.savePlot(plot);

					String rawMessage = messages.get(MessageType.REMOVED_MEMBER_FROM_PLOT);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				} else {
					String rawMessage = messages.get(MessageType.NULL_USER);
					MessageManager.sendColouredMessageToPlayer(rawMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void addPlayerToBlacklist(Player player, String userName, Plot plot){
		if(plot != null) {
			if(plot.getOwnerNickname().equals(player.getName())){
				if(!userName.equals(plot.getOwnerNickname())){
					User user = userManager.getUser(userName);
					if(user != null){
						plot.blacklist.add(userName);
						plotManager.savePlot(plot);
						player.sendMessage(messages.get(MessageType.BLACKLIST_ADDED_TO_PLOT).replace("%plotID%", plot.getID()).replace("%userName%", userName));
					} else {
						String rawMessage = messages.get(MessageType.NULL_USER);
						MessageManager.sendColouredMessageToPlayer(rawMessage, player);
					}
				} else {
					String rawMessage = messages.get(MessageType.CANNOT_ADD_OWNER_TO_BLACKLIST);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void removePlayerFromBlacklist(Player player, String userName, Plot plot){
		if(plot != null) {
			if(plot.getOwnerNickname().equals(player.getName())){
				if(plot.blacklist.contains(userName)){
					plot.blacklist.remove(userName);
					plotManager.savePlot(plot);

					player.sendMessage(messages.get(MessageType.BLACKLIST_REMOVED_FROM_PLOT).replace("%plotID%", plot.getID()).replace("%userName%", userName));
					String rawMessage = messages.get(MessageType.BLACKLIST_REMOVED_FROM_PLOT);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, userManager.getUser(userName));
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				} else {
					String rawMessage = messages.get(MessageType.NULL_USER);
					MessageManager.sendColouredMessageToPlayer(rawMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void createGroup(Player player, String groupName){
		User user = userManager.getUser(player.getName());
		if(user != null){
			if(user.getGroupByName(groupName) == null){
				user.groups.add(new Group(groupName));
				userManager.saveUser(user);

				String rawMessage = messages.get(MessageType.GROUP_CREATE);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName(groupName));
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = messages.get(MessageType.GROUP_ALREADY_EXISTS);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName(groupName));
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_USER);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void deleteGroup(Player player, String groupName){
		User user = userManager.getUser(player.getName());
		if(!groupName.equalsIgnoreCase("all")) {
			if (user != null) {
				Group group = user.getGroupByName(groupName);
				if (group != null) {
					user.groups.remove(group);
					userManager.saveUser(user);

					String rawMessage = messages.get(MessageType.GROUP_DELETE);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				} else {
					String rawMessage = messages.get(MessageType.NULL_GROUP);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = messages.get(MessageType.NULL_USER);
				MessageManager.sendColouredMessageToPlayer(rawMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.CANNOT_DELETE_GROUP);
			String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName("all"));
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		}
	}


	private void addPlotToGroup(Player player, String groupName, Plot plot){
		User user = userManager.getUser(player.getName());
		if(user != null){
			Group group = user.getGroupByName(groupName);
			if(group != null){
				group.plots.add(plot.getUUID());
				userManager.saveUser(user);

				String rawMessage = messages.get(MessageType.PLOT_ADDED_TO_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = messages.get(MessageType.NULL_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_USER);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void removePlotFromGroup(Player player, String groupName, Plot plot){
		User user = userManager.getUser(player.getName());
		if(user != null){
			Group group = user.getGroupByName(groupName);
			if(group != null){
				group.plots.remove(plot.getUUID());
				userManager.saveUser(user);

				String rawMessage = messages.get(MessageType.PLOT_REMOVED_FROM_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = messages.get(MessageType.NULL_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = messages.get(MessageType.NULL_USER);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private List<Plot> getPlotsFromGroupName(Player player, String groupName){
		List<Plot> plots = new ArrayList<>();
		User user = userManager.getUser(player.getName());
		Group group = null;
		for(Group g:user.groups){
			if(g.getName().equalsIgnoreCase(groupName)){
				group = g;
				List<UUID> plotUUIDs = group.plots;
				for(UUID plotUUID:plotUUIDs){
					Plot plot = plotManager.getPlotByUUID(plotUUID);
					if(plot != null) plots.add(plot);
					else group.plots.remove(plotUUID);
				}
				return plots;
			}
		}
		if(group == null){
			String rawMessage = messages.get(MessageType.NULL_GROUP);
			String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		}
		return plots;

	}
}
