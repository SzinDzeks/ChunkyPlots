package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
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
			return PlotPermissionUtil.canPlayerAffectPlot(player, plotEntityIsStandingOn, Flag.SPLASH_POTION_MEMBER, Flag.SPLASH_POTION_STRANGER);
		} else {
			return false;
		}
	}
}
