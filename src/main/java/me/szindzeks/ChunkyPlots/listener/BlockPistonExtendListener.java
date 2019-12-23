package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;


public class BlockPistonExtendListener implements Listener {
    @EventHandler
    public void onBlockPistonExtend(final BlockPistonExtendEvent event){
        if(!PlotPermissionUtil.canPistonAffectBlocks(event.getBlock(), event.getBlocks())){
            event.setCancelled(true);
        }
    }
}
