package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class PlayerUnleashEntityListener implements Listener {
	@EventHandler
	public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event){
		final Location entityLocation = event.getEntity().getLocation();
		final Player player = event.getPlayer();
		final Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlotByLocation(entityLocation);

		if(eventPlot != null) {
			if (!PlotPermissionUtil.canPlayerAffectPlot(player, eventPlot, Flag.ENTITY_LEASH_MEMBER, Flag.ENTITY_LEASH_STRANGER)) {
				event.setCancelled(true);
			}
		}
	}
}
