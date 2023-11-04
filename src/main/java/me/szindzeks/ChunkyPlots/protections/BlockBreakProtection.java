package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakProtection implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(final BlockBreakEvent event){
		if(!canPlayerDestroyBlock(event)){
			event.setCancelled(true);
		}
	}

	private boolean canPlayerDestroyBlock(BlockBreakEvent event) {
		Plot blockPlot = plotManager.getPlotByChunk(event.getBlock().getChunk());
		if(blockPlot != null){
			return PlotPermissionUtil.canPlayerAffectPlot(event.getPlayer(), blockPlot, Flag.BREAK_MEMBER, Flag.BREAK_STRANGER);
		} else {
			return true;
		}
	}
}
