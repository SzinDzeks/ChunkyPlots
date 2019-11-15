package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlockPistonRetractListener implements Listener {
	@EventHandler
	public void onBlockPistonRetract(final BlockPistonRetractEvent event){
		final List<Block> blocks = event.getBlocks();
		final Block piston = event.getBlock();

		PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		Chunk pistonChunk = piston.getChunk();
		Plot pistonPlot = plotManager.getPlot(pistonChunk.getX(), pistonChunk.getZ(), pistonChunk.getWorld().getName());
		String pistonPlotOwner = null;
		if(pistonPlot != null) pistonPlotOwner = pistonPlot.getOwnerNickname();
		for (Plot plot : plotManager.getPlots()) {
			if (plot.getFlags().get(Flag.EXTERNAL_PISTON_PROTECTION) == false) {
				return;
			} else {
				for (final Block block : blocks) {
					Chunk chunk = block.getRelative(event.getDirection().getOppositeFace()).getLocation().getChunk();
					Plot affectedPlot = plotManager.getPlot(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());

					if (affectedPlot == null) continue;
					else if (affectedPlot.getOwnerNickname().equals(pistonPlotOwner)) continue;
					else {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
