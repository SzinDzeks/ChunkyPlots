package me.szindzeks.ChunkyPlots.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import static me.szindzeks.ChunkyPlots.util.PistonUtil.hasPistonPermissionToAffectBlocks;

public class BlockPistonExtendListener implements Listener {
    @EventHandler
    public void onBlockPistonExtend(final BlockPistonExtendEvent event){
        if(!hasPistonPermissionToAffectBlocks(event.getBlock(), event.getBlocks())){
            event.setCancelled(true);
        }
    }
}
