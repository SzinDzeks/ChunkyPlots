package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.Chunk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;

public class AreaEffectCloudApplyListener implements Listener {
	@EventHandler
	public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event){
		ProjectileSource shooter = event.getEntity().getSource();
		if(shooter instanceof Player) {
			Player player = (Player) shooter;
			if(ChunkyPlots.plugin.userManager.getUser(((Player) shooter).getName()).isBypassingRestrictons == true) return;
			Collection<LivingEntity> affectedEntities = event.getAffectedEntities();
			for (LivingEntity entity : affectedEntities) {
				Plot eventPlot = ChunkyPlots.plugin.plotManager.getPlot(entity.getLocation().getChunk());
				if (eventPlot != null)
					if (eventPlot.getOwnerNickname().equals(player.getName())) return;
					else if (eventPlot.getFlags().get(Flag.SPLASH_POTION_MEMBER) == true && eventPlot.members.contains(player.getName())) return;
					else if (eventPlot.getFlags().get(Flag.SPLASH_POTION_STRANGER) == true && !eventPlot.members.contains(player.getName())) return;
					else {
						String message = ChunkyPlots.plugin.configManager.getMessages().get(MessageType.NOT_PERMITTED);
						player.sendMessage(message);
						event.getEntity().setDuration(0);
						event.setCancelled(true);
						break;
					}
			}
		} else {
			event.setCancelled(true);
		}
	}
}
