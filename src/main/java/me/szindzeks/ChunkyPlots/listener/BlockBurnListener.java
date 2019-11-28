package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurnListener implements Listener {
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		Plot ignitingPlot = plotManager.getPlotByChunk(event.getIgnitingBlock().getChunk());
		Plot burnedPlot = plotManager.getPlotByChunk(event.getBlock().getChunk());
		if(ignitingPlot == null && burnedPlot != null) event.setCancelled(true);
		else if(ignitingPlot != null && burnedPlot != null){
			if(!ignitingPlot.getOwnerNickname().equals(burnedPlot.getOwnerNickname()))
				event.setCancelled(true);
		}
 	}
}
