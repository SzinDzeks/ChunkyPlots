package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;

public class AreaEffectCloudApplyListener implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler
	public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event){
		if(!canEffectBeApplied(event)){
			event.setCancelled(true);
		}
	}
	private boolean canEffectBeApplied(AreaEffectCloudApplyEvent event){
		for(LivingEntity entity:event.getAffectedEntities()){
			ProjectileSource shooter = event.getEntity().getSource();
			return canShooterApplyEffectToEntity(shooter, entity);
		}
		return false;
	}

	private boolean canShooterApplyEffectToEntity(ProjectileSource shooter, LivingEntity entity) {
		Plot plotEntityIsStandingOn = plotManager.getPlotByChunk(entity.getLocation().getChunk());
		if(plotEntityIsStandingOn != null){
			return canShooterApplyEffectToEntityStandingOnPlot(shooter, plotEntityIsStandingOn);
		} else {
			return true;
		}
	}

	private boolean canShooterApplyEffectToEntityStandingOnPlot(ProjectileSource shooter, Plot plotEntityIsStandingOn) {
		if(shooter instanceof Player){
			Player player = (Player) shooter;
			return canPlayerApplyEffectToEntityStandingOnPlot(player, plotEntityIsStandingOn);
		} else {
			return false;
		}
	}

	private boolean canPlayerApplyEffectToEntityStandingOnPlot(Player player, Plot plotEntityIsStandingOn) {
		if(plotEntityIsStandingOn.isPlayerOwner(player)){
			return true;
		} else if(plotEntityIsStandingOn.isPlayerMember(player)) {
			return plotEntityIsStandingOn.flags.get(Flag.SPLASH_POTION_MEMBER);
		} else {
			return plotEntityIsStandingOn.flags.get(Flag.SPLASH_POTION_STRANGER);
		}
	}
}
