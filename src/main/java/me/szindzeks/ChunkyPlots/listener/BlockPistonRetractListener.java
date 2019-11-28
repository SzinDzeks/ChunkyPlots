package me.szindzeks.ChunkyPlots.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

import static me.szindzeks.ChunkyPlots.util.PistonUtil.hasPistonPermissionToAffectBlocks;

public class BlockPistonRetractListener implements Listener {
	@EventHandler
	public void onBlockPistonRetract(final BlockPistonRetractEvent event){
		if(!hasPistonPermissionToAffectBlocks(event.getBlock(), event.getBlocks())){
			event.setCancelled(true);
		}
	}
}
