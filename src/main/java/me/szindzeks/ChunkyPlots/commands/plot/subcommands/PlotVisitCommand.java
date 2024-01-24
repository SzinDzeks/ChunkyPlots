package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.basic.VisitPoint;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.*;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotVisitCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;
	final VisitManager visitManager = ChunkyPlots.plugin.visitManager;
	@Override
	public String getName() {
		return "visit";
	}

	@Override
	public String getDescription() {
		return "tworzenie i odwiedzanie punktów wizyt";
	}

	@Override
	public String getSyntax() {
		return "/plot visit";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.visit";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;

			if(args.length == 2) {
				String visitPointName = args[1];
				final VisitPoint visitPoint = visitManager.getVisitPoint(visitPointName);
				if (visitPoint != null) {
					if (visitPoint.isOpen) {
						if (visitPoint.isSafe()) {
							final User user = userManager.getUser(player.getName());
							if (!user.isTeleporting) {
								user.isTeleporting = true;
								String rawMessage = configManager.getMessage(MessageType.TELEPORTING_TO_VISIT_POINT);
								String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
								MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
								final int x = player.getLocation().getBlockX();
								final int y = player.getLocation().getBlockY();
								final int z = player.getLocation().getBlockZ();
								ChunkyPlots.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ChunkyPlots.plugin, () -> {
									if (player.isOnline()) {
										if (player.getLocation().getBlockX() == x && player.getLocation().getBlockY() == y && player.getLocation().getBlockZ() == z) {
											player.teleport(visitPoint.getLocation());
											String rawMessage1 = configManager.getMessage(MessageType.TELEPORTED_TO_VISIT_POINT);
											String uncolouredMessage1 = MessageManager.replacePlaceholders(rawMessage1, visitPoint);
											MessageManager.sendColouredMessageToPlayer(uncolouredMessage1, player);
										} else {
											String rawMessage1 = configManager.getMessage(MessageType.TELEPORT_CANCELLED);
											String uncolouredMessage1 = MessageManager.replacePlaceholders(rawMessage1, visitPoint);
											MessageManager.sendColouredMessageToPlayer(uncolouredMessage1, player);
										}
									}
									user.isTeleporting = false;
								}, 20 * 5);
							} else {
								String rawMessage = configManager.getMessage(MessageType.ALREADY_TELEPORTING);
								MessageManager.sendColouredMessageToPlayer(rawMessage, player);
							}
						} else {
							String rawMessage = configManager.getMessage(MessageType.VISIT_POINT_NOT_SAFE).replace("{visitPointName}", visitPointName);
							MessageManager.sendColouredMessageToPlayer(rawMessage, player);
						}
					} else {
						String rawMessage = configManager.getMessage(MessageType.VISIT_POINT_CLOSED).replace("{visitPointName}", visitPointName);
						String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
						MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					}
				} else {
					String rawMessage = configManager.getMessage(MessageType.NULL_VISIT_POINT).replace("{visitPointName}", visitPointName);
					MessageManager.sendColouredMessageToPlayer(rawMessage, player);
				}
			} else if (args.length == 3){
				if(args[1].equals("createpoint")) createVisitPoint(player, args[2], null, plotManager.getPlotByChunk(player.getLocation().getChunk()));
				else if(args[1].equals("deletepoint")) deleteVisitPoint(player, args[2]);
//				TODO: Add visit help command
			}
		}
	}
	private void createVisitPoint(Player player, String name, String description, Plot plot){
		if(plot != null) {
			for (VisitPoint visitPoint : visitManager.getVisitPoints()) {
				if (visitPoint.getName().equals(name)) {
					String rawMessage = configManager.getMessage(MessageType.VISIT_POINT_ALREADY_EXISTS);
					String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
					MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
					return;
				}
			}
			Location location = player.getLocation();
			VisitPoint visitPoint = new VisitPoint(location, plot.getUUID(), player.getName(), name, description);
			visitManager.createVisitPoint(visitPoint);

			String rawMessage = configManager.getMessage(MessageType.CREATED_VISIT_POINT);
			String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		} else {
			String rawMessage = configManager.getMessage(MessageType.VISIT_POINT_NOT_INSIDE_PLOT);
			MessageManager.sendColouredMessageToPlayer(rawMessage, player);
		}
	}

	private void deleteVisitPoint(Player player, String visitPointName){
		VisitPoint visitPoint = visitManager.getVisitPoint(visitPointName);
		if(visitPoint != null) {
			if(visitPoint.getOwnerName().equals(player.getName())){
				visitManager.deleteVisitPoint(visitPoint);

				String rawMessage = configManager.getMessage(MessageType.DELETED_VISIT_POINT);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			} else {
				String rawMessage = configManager.getMessage(MessageType.NOT_VISIT_POINT_OWNER);
				String uncolouredMessage = MessageManager.replacePlaceholders(rawMessage, visitPoint, player);
				MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
			}
		} else {
			String rawMessage = configManager.getMessage(MessageType.NULL_VISIT_POINT);
			String uncolouredMessage = rawMessage.replace("{visitPointName}", visitPointName);
			MessageManager.sendColouredMessageToPlayer(uncolouredMessage, player);
		}
	}
}
