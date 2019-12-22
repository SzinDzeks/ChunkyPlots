package me.szindzeks.ChunkyPlots.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

import static me.szindzeks.ChunkyPlots.util.PlotPermissionUtil.canPistonAffectBlocks;

public class BlockPistonRetractListener implements Listener {
	@EventHandler
	public void onBlockPistonRetract(final BlockPistonRetractEvent event){
		if(!canPistonAffectBlocks(event.getBlock(), event.getBlocks())){
			event.setCancelled(true);
		}
	}
}
