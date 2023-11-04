package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener implements Listener {
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
		Entity clickedEntity = event.getRightClicked();
		Player player = event.getPlayer();
		Location clickedEntityLocation = clickedEntity.getLocation();
		Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByChunk(clickedEntityLocation.getChunk());

		if(eventPlot != null) {
			if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.ENTITY_INTERACT_MEMBER, Flag.ENTITY_INTERACT_STRANGER)) {
				event.setCancelled(true);
			}
		}
	}
}
