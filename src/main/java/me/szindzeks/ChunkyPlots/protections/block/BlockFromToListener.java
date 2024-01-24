package me.szindzeks.ChunkyPlots.protections.block;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event){
		if(!canBlockMoveFromTo(event.getBlock(), event.getToBlock())){
			event.setCancelled(true);
		}
	}

	private boolean canBlockMoveFromTo(Block block, Block toBlock) {
		//This event is handling only two types of blocks: liquids and dragon eggs
		if(block.getType().equals(Material.DRAGON_EGG)){
			return canDragonEggMoveFromTo(block, toBlock);
		} else {
			return canLiquidMoveFromTo(block, toBlock);
		}
	}

	private boolean canLiquidMoveFromTo(Block block, Block toBlock) {
		Plot fromPlot = plotManager.getPlotByChunk(block.getChunk());
		Plot toPlot = plotManager.getPlotByChunk(toBlock.getChunk());
		if(fromPlot == null && toPlot == null) {
			return true;
		} else if(fromPlot != null && toPlot == null){
			return true;
		} else if(fromPlot == null && toPlot != null){
			return false;
		} else return fromPlot.hasTheSameOwnerAs(toPlot);
	}

	private boolean canDragonEggMoveFromTo(Block block, Block toBlock) {
		Plot fromPlot = plotManager.getPlotByChunk(block.getChunk());
		Plot toPlot = plotManager.getPlotByChunk(toBlock.getChunk());
		if(fromPlot == null && toPlot == null) {
			return true;
		} else if(fromPlot != null && toPlot == null){
			return false;
		} else if(fromPlot == null && toPlot != null){
			return false;
		} else return fromPlot.hasTheSameOwnerAs(toPlot);
	}
}
