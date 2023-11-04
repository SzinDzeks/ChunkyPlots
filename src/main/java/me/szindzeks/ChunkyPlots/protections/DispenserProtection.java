package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class DispenserProtection implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
	@EventHandler
	public void onDispense(BlockDispenseEvent event){
		if(!canBlockDispense(event)){
			event.setCancelled(true);
		}
	}

	private boolean canBlockDispense(BlockDispenseEvent event) {
		Plot sourcePlot = getEventSourcePlot(event);
		Plot destinationPlot = getEventDestinationPlot(event);
		return canPlotDispenseOnPlot(sourcePlot, destinationPlot);
	}

	private Plot getEventSourcePlot(BlockDispenseEvent event) {
		Block source = event.getBlock();
		return plotManager.getPlotByChunk(source.getChunk());
	}

	private Plot getEventDestinationPlot(BlockDispenseEvent event) {
		int x = event.getVelocity().getBlockX();
		int y = event.getVelocity().getBlockY();
		int z = event.getVelocity().getBlockZ();
		World world = event.getBlock().getWorld();
		Block destination = world.getBlockAt(x, y, z);

		return plotManager.getPlotByChunk(destination.getChunk());
	}

	private boolean canPlotDispenseOnPlot(Plot sourcePlot, Plot destinationPlot) {
		if(sourcePlot == null && destinationPlot != null){
			return false;
		} else if(sourcePlot != null && destinationPlot != null) {
			return sourcePlot.getOwnerNickname().equals(destinationPlot.getOwnerNickname());
		}
		return true;
	}
}
