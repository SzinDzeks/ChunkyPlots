package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;

public class PlayerBucketEmptyListener implements Listener {
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		Block block = event.getBlockClicked();
		if(block != null){
			Plot plot = ChunkyPlots.plugin.plotManager.getPlotByChunk(block.getChunk());
			Player player = event.getPlayer();
			if(plot != null) {
				if (!plot.getOwnerNickname().equals(player.getName())) {
					if (!plot.members.contains(player.getName())) {
						if (!plot.getFlags().get(Flag.PLACE_STRANGER)) {
							event.setCancelled(true);
						}
					} else if (!plot.getFlags().get(Flag.PLACE_MEMBER)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
