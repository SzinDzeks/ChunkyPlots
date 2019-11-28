package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		final Block block = event.getClickedBlock();
		final Player player = event.getPlayer();

		if(ChunkyPlots.plugin.userManager.getUser(player.getName()).isBypassingRestrictons == true) return;
		else if(block != null) {
			final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(block.getLocation().getChunk());

			if (eventPlot == null) return;
			else if (ChunkyPlots.plugin.userManager.getUser(player.getName()).cooldown == true) {
				event.setCancelled(true);
			} else {
				if (eventPlot.getOwnerNickname().equals(player.getName())) return;
				else if (eventPlot.getFlags().get(Flag.BLOCK_INTERACT_MEMBER) == true && eventPlot.members.contains(player.getName()))
					return;
				else if (eventPlot.getFlags().get(Flag.BLOCK_INTERACT_STRANGER) == true && !eventPlot.members.contains(player.getName()))
					return;
				else {
					String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
					player.sendMessage(message);
					event.setCancelled(true);
				}
			}
		}
	}
}
