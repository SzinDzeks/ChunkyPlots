package me.szindzeks.ChunkyPlots.protections.block;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.CraftingManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.PlayerInventory;

public class BlockPlaceListener implements Listener {
    private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
    private final ConfigManager configManager = ChunkyPlots.plugin.configManager;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event){
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());

        if(blockPlot != null){
            if(!PlotPermissionUtil.canPlayerAffectPlot(player, blockPlot, Flag.PLACE_MEMBER, Flag.PLACE_STRANGER)){
                event.setCancelled(true);
                String message = configManager.getMessage(MessageType.NOT_PERMITTED);
                player.sendMessage(message);
            } else {
                if(hasPlayerPlacedAPlotBlock(player, block)){
                    event.setCancelled(true);
                    String message = configManager.getMessage(MessageType.PLOT_ALREADY_EXISTS);
                    player.sendMessage(message);
                }
            }
        } else {
            if(shouldPlotBeCreated(event)){
                if(!hasBlockBeenPlacedInRestrictedArea(block)) {
                    plotManager.claimPlot(player, block);
                    block.setType(Material.AIR);
                } else {
                    String message = configManager.getMessage(MessageType.NOT_PERMITTED);
                    player.sendMessage(message);
                }
            }
        }
    }

    private boolean shouldPlotBeCreated(BlockPlaceEvent event) {
        if(!event.isCancelled()){
            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();
			return hasPlayerPlacedAPlotBlock(player, block);
        }
        return false;
    }
    private boolean hasPlayerPlacedAPlotBlock(Player player, Block block) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.getItemInMainHand().isSimilar(CraftingManager.plotBlock) || inventory.getItemInOffHand().isSimilar(CraftingManager.plotBlock)) {
			return block.getType().equals(Material.NOTE_BLOCK);
        }
        return false;
    }

    private boolean hasBlockBeenPlacedInRestrictedArea(Block block) {
		return block.getWorld().getName().equals("world_the_end");
	}

}
