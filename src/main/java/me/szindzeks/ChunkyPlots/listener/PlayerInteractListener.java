package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		final Block block = event.getClickedBlock();
		final Player player = event.getPlayer();
		final Plot eventPlot;
		if(block != null) {
			eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(block.getChunk());
			if (eventPlot != null) {
				if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.BLOCK_INTERACT_MEMBER, Flag.BLOCK_INTERACT_STRANGER)) {
					event.setCancelled(true);
				}
			}
		}
	}
}
