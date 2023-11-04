package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;

public class PlayerTakeLecternBookListener implements Listener {
	@EventHandler
	public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event){
		final Location lecternLocation = event.getLectern().getLocation();
		final Player player = event.getPlayer();
		final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(lecternLocation.getChunk());

		if(eventPlot != null) {
			if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.BLOCK_INTERACT_MEMBER, Flag.BLOCK_INTERACT_STRANGER)) {
				event.setCancelled(true);
			}
		}
	}
}
