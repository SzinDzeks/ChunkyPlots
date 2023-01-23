package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import me.szindzeks.ChunkyPlots.manager.VisitManager;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
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
		return "description";
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
		if(sender instanceof Player) {
			Player player = (Player) sender;
 			Plot plot = plotManager.getPlotByChunk(player.getLocation().getChunk());
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
			} else player.sendMessage(configManager.getMessage(MessageType.NULL_PLOT));
		}
	}
}
