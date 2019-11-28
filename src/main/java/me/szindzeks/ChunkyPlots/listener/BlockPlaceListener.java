package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.CraftingManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockPlaceListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event){
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        {
            UserManager userManager = ChunkyPlots.plugin.userManager;
            final User user = userManager.getUser(player.getName());

            if (user.cooldown == true) {
                event.setCancelled(true);
                return;
            } else if (user.isBypassingRestrictons == true) return;
        }

        PlotManager plotManager = ChunkyPlots.plugin.plotManager;
        ConfigManager configManager = ChunkyPlots.plugin.configManager;

        Plot plot = plotManager.getPlotByCoordinates(block.getChunk().getX(), block.getChunk().getZ(), block.getWorld().getName());
        if (plot != null) {
            if (plot.getFlags().get(Flag.PLACE_STRANGER) == true && !plot.members.contains(player.getName())) {
            } else if (plot.getOwnerNickname().equals(player.getName())) {
            } else if (plot.members.contains(player.getName()) && plot.getFlags().get(Flag.PLACE_MEMBER) == true) {
            } else {
                event.setCancelled(true);
                String message = configManager.getMessages().get(MessageType.NOT_PERMITTED);
                player.sendMessage(message);
            }
        }

        if(event.isCancelled() == false) {
            PlayerInventory inventory = event.getPlayer().getInventory();
            if (inventory.getItemInMainHand().isSimilar(CraftingManager.plotBlock) || inventory.getItemInOffHand().isSimilar(CraftingManager.plotBlock)) {
                plotManager.claimPlot(player, event.getBlock());
                event.getBlockPlaced().setType(Material.AIR);
            }
        }
    }
}
