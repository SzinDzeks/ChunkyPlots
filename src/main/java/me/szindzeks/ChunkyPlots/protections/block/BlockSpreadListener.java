package me.szindzeks.ChunkyPlots.protections.block;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event){
		PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		Plot sourceBlockPlot = plotManager.getPlotByChunk(event.getSource().getChunk());
		Plot destinationBlockPlot = plotManager.getPlotByChunk(event.getBlock().getChunk());
		if(!canBlockSpreadFromPlotToPlot(sourceBlockPlot, destinationBlockPlot)){
			event.setCancelled(true);
		}
	}

	private boolean canBlockSpreadFromPlotToPlot(Plot sourceBlockPlot, Plot destinationBlockPlot) {
		if(sourceBlockPlot == null && destinationBlockPlot == null) {
			return true;
		} else if(sourceBlockPlot != null && destinationBlockPlot != null){
			return sourceBlockPlot.hasTheSameOwnerAs(destinationBlockPlot);
		}
		return false;
	}
}
