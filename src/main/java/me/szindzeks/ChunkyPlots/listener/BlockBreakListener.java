package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.manager.UserManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(final BlockBreakEvent event){
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

		Plot plot = plotManager.getPlotByCoordinates(block.getLocation().getChunk().getX(), block.getLocation().getChunk().getZ(), block.getWorld().getName());
		if (plot != null) {
			if (plot.getFlags().get(Flag.BREAK_STRANGER) == true && !plot.members.contains(player.getName())) {
			} else if (plot.getOwnerNickname().equals(player.getName())) {
			} else if (plot.members.contains(player.getName()) && plot.getFlags().get(Flag.BREAK_MEMBER) == true) {
			} else {
				event.setCancelled(true);

				String message = configManager.getMessages().get(MessageType.NOT_PERMITTED);
				player.sendMessage(message);
			}
		}
	}
}
