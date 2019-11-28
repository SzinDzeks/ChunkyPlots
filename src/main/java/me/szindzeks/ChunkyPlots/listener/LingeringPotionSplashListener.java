package me.szindzeks.ChunkyPlots.listener;

import com.google.gson.internal.$Gson$Preconditions;
import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;

public class LingeringPotionSplashListener implements Listener {
	@EventHandler
	public void onLingeringPotionSplash(LingeringPotionSplashEvent event){
		Location location = event.getEntity().getLocation();
		Plot plot = ChunkyPlots.plugin.plotManager.getPlotByChunk(location.getChunk());
		if(plot != null){
			ProjectileSource shooter = event.getEntity().getShooter();
			if(shooter instanceof  Player){
				Player player = (Player) shooter;
				if(ChunkyPlots.plugin.userManager.getUser(((Player) shooter).getName()).isBypassingRestrictons == true) return;
				else if (plot.getOwnerNickname().equals(player.getName())) return;
				else if (plot.getFlags().get(Flag.SPLASH_POTION_MEMBER) == true && plot.members.contains(player.getName())) return;
				else if (plot.getFlags().get(Flag.SPLASH_POTION_STRANGER) == true && !plot.members.contains(player.getName())) return;
				else {
					String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
					player.sendMessage(message);
					event.setCancelled(true);
				}
			} else event.setCancelled(true);
		}
	}
}
