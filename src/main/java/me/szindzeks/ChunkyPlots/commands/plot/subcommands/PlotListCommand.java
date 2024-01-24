package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.manager.MessageManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotListCommand extends Subcommand {
	final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public String getDescription() {
		return "lista działek gracza";
	}

	@Override
	public String getSyntax() {
		return "/plot list";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player player){
			player.sendMessage("");
			player.sendMessage("");
			MessageManager.sendNoPrefixMessage(player, "&eID Działki &9» &ePrzybliżone koordynaty działki");
			for(Plot plot:plotManager.getPlots()){
				if(plot.getOwnerNickname().equals(player.getName())) {
					Location location = player.getWorld().getChunkAt(plot.getChunkX(), plot.getChunkZ()).getBlock(8, 64, 8).getLocation();
					MessageManager.sendNoPrefixMessage(player,"&8(&6" + plot.getID() + "&8)" + " &a» &7X:&f" + location.getBlockX() + "  &7Y:&f" + location.getBlockY() + "  &7Z:&f" + location.getBlockZ());
				}
			}
			player.sendMessage("");
			player.sendMessage("");
		}
	}
}
