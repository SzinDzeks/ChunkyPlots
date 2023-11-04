package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurnProtection implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		if(!blockCanBeBurntByFire(event.getBlock(), event.getIgnitingBlock())){
			event.setCancelled(true);
		}
 	}

	private boolean blockCanBeBurntByFire(Block block, Block fire) {
		if(fire != null) {
			Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
			Plot firePlot = plotManager.getPlotByChunk(fire.getChunk());
			if(blockPlot != null) {
				if(firePlot != null) {
					if (blockPlot.hasTheSameOwnerAs(firePlot)) {
						return true;
					} else {
						return !blockPlot.flags.get(Flag.EXTERNAL_FIRE_PROTECTION);
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
}
