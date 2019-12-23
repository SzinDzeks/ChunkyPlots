package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockPistonRetractListener implements Listener {
	@EventHandler
	public void onBlockPistonRetract(final BlockPistonRetractEvent event){
		if(!PlotPermissionUtil.canPistonAffectBlocks(event.getBlock(), event.getBlocks())){
			event.setCancelled(true);
		}
	}
}
