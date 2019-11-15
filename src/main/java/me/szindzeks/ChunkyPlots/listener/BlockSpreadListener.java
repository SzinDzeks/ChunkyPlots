package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event){
		PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		Plot sourceBlockPlot = plotManager.getPlot(event.getSource().getChunk());
		Plot destinationBlockPlot = plotManager.getPlot(event.getBlock().getChunk());
		if(sourceBlockPlot == null && destinationBlockPlot != null) event.setCancelled(true);
		else if(sourceBlockPlot != null && destinationBlockPlot != null)
			if(!sourceBlockPlot.getOwnerNickname().equals(destinationBlockPlot.getOwnerNickname())) event.setCancelled(true);
	}
}
