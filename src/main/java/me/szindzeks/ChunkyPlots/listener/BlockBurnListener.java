package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurnListener implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		if(!blockCanBeBurntByFire(event.getBlock(), event.getIgnitingBlock())){
			event.setCancelled(true);
		}
 	}

	private boolean blockCanBeBurntByFire(Block block, Block fire) {
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
		Plot firePlot = plotManager.getPlotByChunk(fire.getChunk());
		if(blockPlot.equals(firePlot)){
			return true;
		} else if(blockPlot.getOwnerNickname().equals(firePlot.getOwnerNickname())) {
			return true;
		} else {
			return !blockPlot.flags.get(Flag.EXTERNAL_FIRE_PROTECTION);
		}
	}
}
