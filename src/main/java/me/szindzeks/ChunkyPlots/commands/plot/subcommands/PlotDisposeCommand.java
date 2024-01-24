package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.*;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.*;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlotDisposeCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	final UserManager userManager = ChunkyPlots.plugin.userManager;
	final VisitManager visitManager = ChunkyPlots.plugin.visitManager;

	@Override
	public String getName() {
		return "dispose";
	}

	@Override
	public String getDescription() {
		return "usuwa działkę i zwraca blok działki";
	}

	@Override
	public String getSyntax() {
		return "/plot dispose";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;

			Chunk chunk = player.getLocation().getChunk();
			String plotID = chunk.getX() + ";" + chunk.getZ();
			Plot plot = plotManager.getPlotByChunk(chunk);
			if(plot != null){
				if(plot.getOwnerNickname().equals(player.getName())) {
					plotManager.disposePlot(plot);
					player.getInventory().addItem(CraftingManager.plotBlock);
					player.sendMessage(configManager.getMessage(MessageType.PLOT_DELETED).replace("{plotID}", plotID).replace("{world}", plot.getWorldName()));

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
						MessageManager.sendMessage(player, "&cNa usuniętej działce znajdował się punkt &f" + visitPoint.getName() + "&c, więc został on usunięty!");
					}
				} else player.sendMessage(configManager.getMessage(MessageType.NOT_OWNER));
			} else player.sendMessage(configManager.getMessage(MessageType.NULL_PLOT).replace("{plotID}", plotID));

		} else {
			String message = ChunkyPlots.plugin.configManager.getMessage(MessageType.SENDER_NOT_PLAYER);
			sender.sendMessage(message);
		}
	}
}
