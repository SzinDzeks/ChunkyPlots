package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.MessageManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotInfoCommand extends Subcommand {
	final ConfigManager configManager = ChunkyPlots.plugin.configManager;
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getDescription() {
		return "wyświetla informacje o działce";
	}

	@Override
	public String getSyntax() {
		return "/plot info";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player player) {
 			Plot plot = plotManager.getPlotByChunk(player.getLocation().getChunk());
			if (plot != null) {
				MessageManager.sendMessage(player, "&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------");
				MessageManager.sendMessage(player, "&aWłaściciel: &f" + plot.getOwnerNickname());
				MessageManager.sendMessage(player, "&aChunk X: &f" + plot.getChunkX());
				MessageManager.sendMessage(player, "&aChunk Z: &f" + plot.getChunkZ());
				MessageManager.sendMessage(player, "&aŚwiat: &f" + plot.getWorldName());
				MessageManager.sendMessage(player, "&aCzłonkowie: &f" + plot.members.toString());
				MessageManager.sendMessage(player, "&aZablokowani: &f" + plot.blacklist.toString());
				Location location = player.getWorld().getChunkAt(plot.getChunkX(), plot.getChunkZ()).getBlock(8, 64, 8).getLocation();
				MessageManager.sendMessage(player, "&aLokalizacja: &7X:&f" + location.getBlockX() + "  &7Y:&f" + location.getBlockY() + "  &7Z:&f" + location.getBlockZ());
				MessageManager.sendMessage(player, "&aUUID: &f" + plot.getUUID());
				MessageManager.sendMessage(player, "&9-----------{ " + configManager.getPluginPrefix() + " &9}-----------");
			} else player.sendMessage(configManager.getMessage(MessageType.NULL_PLOT));
		}
	}
}
