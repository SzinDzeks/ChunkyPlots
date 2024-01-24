package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PlotMembersCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;

	@Override
	public String getName() {
		return "members";
	}

	@Override
	public String getDescription() {
		return "zarządzanie członkami działki";
	}

	@Override
	public String getSyntax() {
		return "/plot members";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.members";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player player) {
			if(args.length == 3) {
					if (args[1].equals("add"))
						addMemberToPlot(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
					else if (args[1].equals("remove"))
						removeMemberFromPlot(player, args[2], plotManager.getPlotByChunk(player.getLocation().getChunk()));
	//			TODO: add members help command
			} else if(args.length == 4){
				if (args[1].equals("add")) {
					List<Plot> plots = PlotGroupCommand.getPlotsFromGroupName(player, args[3]);
					if(plots.size() > 0) for (Plot plot : plots) addMemberToPlot(player, args[2], plot);
					else MessageManager.sendMessage(player, "&cDo tej grupy nie są przypisane żadne działki");
				}
				else if (args[1].equals("remove")) {
					List<Plot> plots = PlotGroupCommand.getPlotsFromGroupName(player, args[3]);
					if(plots.size() > 0) for (Plot plot : plots) removeMemberFromPlot(player, args[2], plot);
					else MessageManager.sendMessage(player, "&cDo tej grupy nie są przypisane żadne działki");
				}
			} else if(args.length == 6){
				if (args[1].equals("add")) addMemberToPlot(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
				else if (args[1].equals("remove"))removeMemberFromPlot(player, args[2], plotManager.getPlotByCoordinates(args[3], args[4], args[5]));
			}
		}
	}

	private void addMemberToPlot(Player player, String userName, Plot plot) {
		if (plot != null) {
			if (plot.getOwnerNickname().equals(player.getName())) {
				if (!userName.equals(plot.getOwnerNickname())) {
					User user = userManager.getUser(userName);
					if (user != null) {
						if(!plot.members.contains(userName)) {
							plot.members.add(userName);
							plotManager.savePlot(plot);
							String rawMessage = configManager.getMessage(MessageType.ADDED_MEMBER_TO_PLOT);
							String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, user);
							MessageManager.sendMessage(player, uncolouredMessage);
						} else {
							String rawMessage = configManager.getMessage(MessageType.PLAYER_IS_ALREADY_A_MEMBER);
							String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, user);
							MessageManager.sendMessage(player, uncolouredMessage);
						}
					} else {
						String rawMessage = configManager.getMessage(MessageType.NULL_USER).replace("{userName}", userName);
						MessageManager.sendMessage(player, rawMessage);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.CANNOT_ADD_OWNER_AS_MEMBER);
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


	private void removeMemberFromPlot(Player player, String userName, Plot plot){
		if(plot != null) {
			if(plot.getOwnerNickname().equals(player.getName())){
				if(plot.members.contains(userName)){
					plot.members.remove(userName);
					plotManager.savePlot(plot);

					User user = userManager.getUser(userName);
					String rawMessage = configManager.getMessage(MessageType.REMOVED_MEMBER_FROM_PLOT);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, plot, user);
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
}
