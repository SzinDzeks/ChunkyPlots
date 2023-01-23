package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Group;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlotGroupCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;
	final VisitManager visitManager = ChunkyPlots.plugin.visitManager;

	@Override
	public String getName() {
		return "group";
	}

	@Override
	public String getDescription() {
		return "Pozwala na zarządzenie grupami działek";
	}

	@Override
	public String getSyntax() {
		return "/plot group";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 2) {
				if(args[1].equals("list")){
					sendGroupListToPlayer(player);
				}
			} else if(args.length == 3){
				switch (args[1]) {
					case "create":
						createGroup(player, args[2]);
						break;
					case "delete":
						deleteGroup(player, args[2]);
						break;
					case "add":
						addPlotToGroup(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
						break;
					case "remove":
						removePlotFromGroup(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
						break;
					case "info":
						sendGroupInfoToPlayer(args[2], player);
						break;
				}
	//			TODO: Add group help message

			} else if(args.length == 6){
				if (args[1].equals("add")) addPlotToGroup(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
				else if (args[1].equals("remove")) removePlotFromGroup(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
			}
		}
	}

	private void sendGroupInfoToPlayer(String groupName, Player player) {
		Group group = userManager.getUser(player.getName()).getGroupByName(groupName);
		List<UUID> plotUUIDs = group.plots;
		for(UUID uuid:plotUUIDs){
			player.sendMessage(uuid.toString());
		}
	}

	private void sendGroupListToPlayer(Player player) {
		List<Group> groups = userManager.getUser(player.getName()).getGroups();
		for (Group g: groups){
			player.sendMessage(g.getName());
		}
	}

	private void createGroup(Player player, String groupName){
		User user = userManager.getUser(player.getName());
		if(user != null){
			if(user.getGroupByName(groupName) == null){
				user.groups.add(new Group(groupName));
				userManager.saveUser(user);

				String rawMessage = configManager.getMessage(MessageType.GROUP_CREATE);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName(groupName));
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = configManager.getMessage(MessageType.GROUP_ALREADY_EXISTS);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName(groupName));
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_USER);
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

					String rawMessage = configManager.getMessage(MessageType.GROUP_DELETE);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				} else {
					group = new Group(groupName);
					String rawMessage = configManager.getMessage(MessageType.NULL_GROUP);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				String rawMessage = configManager.getMessage(MessageType.NULL_USER);
				MessageManager.sendColouredMessageToPlayer(rawMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.CANNOT_DELETE_DEFAULT_GROUP);
			String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, user.getGroupByName("all"));
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		}
	}


	private void addPlotToGroup(Player player, String groupName, Plot plot){
		User user = userManager.getUser(player.getName());
		if(user != null){
			Group group = user.getGroupByName(groupName);
			if(group != null){
				if(!group.getName().equals("all")) {
					if(!group.plots.contains(plot.getUUID())) {
						group.plots.add(plot.getUUID());
						userManager.saveUser(user);

						String rawMessage = configManager.getMessage(MessageType.PLOT_ADDED_TO_GROUP);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, group);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					} else {
						String rawMessage = configManager.getMessage(MessageType.PLOT_ALREADY_IN_GROUP);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.CANNOT_ADD_PLOT_TO_DEFAULT_GROUP);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				group = new Group(groupName);
				String rawMessage = configManager.getMessage(MessageType.NULL_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_USER);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}


	private void removePlotFromGroup(Player player, String groupName, Plot plot){
		User user = userManager.getUser(player.getName());
		if(user != null){
			Group group = user.getGroupByName(groupName);
			if(group != null){
				if(!group.getName().equals("all")) {
					if(group.plots.contains(plot.getUUID())){
						group.plots.remove(plot.getUUID());
						userManager.saveUser(user);

						String rawMessage = configManager.getMessage(MessageType.PLOT_REMOVED_FROM_GROUP);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, group);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					} else {
						String rawMessage = configManager.getMessage(MessageType.PLOT_NOT_IN_GROUP);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.CANNOT_REMOVE_PLOT_FROM_DEFAULT_GROUP);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
				}
			} else {
				group = new Group(groupName);
				String rawMessage = configManager.getMessage(MessageType.NULL_GROUP);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, group);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_USER);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	public static List<Plot> getPlotsFromGroupName(Player player, String groupName){
		List<Plot> plots = new ArrayList<>();
		User user = ChunkyPlots.plugin.userManager.getUser(player.getName());
		Group group = null;
		for(Group g:user.groups){
			if(g.getName().equalsIgnoreCase(groupName)){
				group = g;
				List<UUID> plotUUIDs = group.plots;
				for(UUID plotUUID:plotUUIDs){
					Plot plot = ChunkyPlots.plugin.plotManager.getPlotByUUID(plotUUID);
					if(plot != null) plots.add(plot);
					else group.plots.remove(plotUUID);
				}
				return plots;
			}
		}
		String rawMessage = ChunkyPlots.plugin.configManager.getMessage(MessageType.NULL_GROUP);
		String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
		MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		return plots;

	}
}
