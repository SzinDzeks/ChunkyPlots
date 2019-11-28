package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event){
		PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		Block from = event.getBlock();
		Block to = event.getToBlock();
		Plot fromPlot = plotManager.getPlotByChunk(from.getChunk());
		Plot toPlot = plotManager.getPlotByChunk(to.getChunk());
		if(from.getType().equals(Material.DRAGON_EGG)){
			if(fromPlot == null && toPlot == null) return;
			else if(fromPlot != null && toPlot == null) event.setCancelled(true);
			else if(fromPlot == null && toPlot != null) event.setCancelled(true);
			else if(!fromPlot.getOwnerNickname().equals(toPlot.getOwnerNickname()))  event.setCancelled(true);
		} else {
			if(fromPlot == null && toPlot == null) return;
			else if(fromPlot != null && toPlot == null) return;
			else if(fromPlot == null && toPlot != null) event.setCancelled(true);
			else if(!fromPlot.getOwnerNickname().equals(toPlot.getOwnerNickname()))  event.setCancelled(true);
		}
	}
}
