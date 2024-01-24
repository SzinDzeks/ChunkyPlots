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

public class PlotBlacklistCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;
	final VisitManager visitManager = ChunkyPlots.plugin.visitManager;
	@Override
	public String getName() {
		return "blacklist";
	}

	@Override
	public String getDescription() {
		return "zarzrądzanie blokowaniem dostępu do dziąłki";
	}

	@Override
	public String getSyntax() {
		return "/plot blacklist";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.blacklist";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player player){
			if(args.length == 3) {
				if (args[1].equals("add"))
					addPlayerToBlacklist(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
				else if (args[1].equals("remove"))
					removePlayerFromBlacklist(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
//			TODO: Add help for blacklist command
			} else if (args.length == 4){
//				TODO: Fix player not exist message appearing more than one time due to multiple plots
//				TODO: Fix being able to blacklist a member
//				TODO: Fix player unable to move after being blacklisted
//				TODO: Fix being able to add one player to blacklist multiple times
				if (args[1].equals("add")) {
					List<Plot> plots = getPlotsFromGroupName(player, args[3]);
					if(!plots.isEmpty()) for(Plot plot: plots) addPlayerToBlacklist(player, args[2], plot );
					else MessageManager.sendMessage(player, "&cDo tej grupy nie są przypisane żadne działki");
				}
				else if (args[1].equals("remove")) {
					List<Plot> plots = getPlotsFromGroupName(player, args[3]);
					if(!plots.isEmpty()) for(Plot plot: plots) removePlayerFromBlacklist(player, args[2], plot);
					else MessageManager.sendMessage(player, "&cDo tej grupy nie są przypisane żadne działki");
				}
			} else if(args.length == 6){
				if (args[1].equals("add")) addPlayerToBlacklist(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
				else if (args[1].equals("remove")) removePlayerFromBlacklist(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
			}
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
						player.sendMessage(configManager.getMessage(MessageType.BLACKLIST_ADDED_TO_PLOT).replace("%plotID%", plot.getID()).replace("%userName%", userName));
					} else {
						String rawMessage = configManager.getMessage(MessageType.NULL_USER).replace("{userName}", userName);
						MessageManager.sendMessage(player, rawMessage);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.CANNOT_ADD_OWNER_TO_BLACKLIST);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
					MessageManager.sendMessage(player, uncolouredMessage);
				}
			} else {
				String rawMessage = configManager.getMessage(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendMessage(player, uncolouredMessage);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_PLOT);
			MessageManager.sendMessage(player, rawMessage);
		}
	}


	private void removePlayerFromBlacklist(Player player, String userName, Plot plot){
		if(plot != null) {
			if(plot.getOwnerNickname().equals(player.getName())){
				if(plot.blacklist.contains(userName)){
					plot.blacklist.remove(userName);
					plotManager.savePlot(plot);

					player.sendMessage(configManager.getMessage(MessageType.BLACKLIST_REMOVED_FROM_PLOT).replace("%plotID%", plot.getID()).replace("%userName%", userName));
					String rawMessage = configManager.getMessage(MessageType.BLACKLIST_REMOVED_FROM_PLOT);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, userManager.getUser(userName));
					MessageManager.sendMessage(player, uncolouredMessage);
				} else {
					String rawMessage = configManager.getMessage(MessageType.NULL_USER).replace("{userName}", userName);
					MessageManager.sendMessage(player, rawMessage);
				}
			} else {
				String rawMessage = configManager.getMessage(MessageType.NOT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, player);
				MessageManager.sendMessage(player, uncolouredMessage);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_PLOT);
			MessageManager.sendMessage(player, rawMessage);
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
		String rawMessage = configManager.getMessage(MessageType.NULL_GROUP);
		String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, player);
		MessageManager.sendMessage(player, uncolouredMessage);
		return plots;

	}
}

