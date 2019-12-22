package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
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
			return canPlayerDestroyBlockOnPlot(event.getPlayer(), blockPlot);
		} else {
			return true;
		}
	}

	private boolean canPlayerDestroyBlockOnPlot(Player player, Plot blockPlot) {
		if(blockPlot.isPlayerOwner(player)){
			return true;
		} else if(blockPlot.isPlayerMember(player)) {
			return blockPlot.flags.get(Flag.BREAK_MEMBER);
		} else {
			return blockPlot.flags.get(Flag.BREAK_STRANGER);
		}
	}
}
