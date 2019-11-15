package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class EntityExplodeListener implements Listener {
	@EventHandler
	public void onEntityExplode(final EntityExplodeEvent event){
		final PlotManager plotManager = ChunkyPlots.plugin.plotManager;
		final List<Plot> plots = plotManager.getPlots();

		for (final Block block : event.blockList()) {
			for (final Plot plot : plots) {
				if (plot.isInside(block.getLocation())) {
					if (event.getEntity() instanceof TNTPrimed) {
						final Entity igniter = ((TNTPrimed) event.getEntity()).getSource();

						if (igniter instanceof Player) {
							final User user = ChunkyPlots.plugin.userManager.getUser(igniter.getName());


							if (plot.getOwnerNickname().equals(user.getNickname())) continue;
							else if (plot.members.contains(user.getNickname())) continue;
						}
					}
					event.setCancelled(true);
				}
			}
		}

	}
}
