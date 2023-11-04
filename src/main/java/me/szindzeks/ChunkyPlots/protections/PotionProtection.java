package me.szindzeks.ChunkyPlots.protections;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionProtection implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	@EventHandler
	public void onPotionSplash(final PotionSplashEvent event){
		if(!canPotionBeSplashed(event)){
			event.setCancelled(true);
		}
	}
	private boolean canPotionBeSplashed(final PotionSplashEvent event) {
		ProjectileSource shooter = event.getPotion().getShooter();
		if(shooter instanceof Player){
			return canPlayerSplashPotion(event);
		} else if(shooter instanceof BlockProjectileSource){
			return canBlockSplashPotion(event);
		} else if(shooter instanceof LivingEntity){
			return canLivingEntitySplashPotion(event);
		}
		return false;
	}
	private boolean canPlayerSplashPotion(final PotionSplashEvent event) {
		Player player = (Player) event.getPotion().getShooter();
		List<Plot> affectedEntitesPlots = getAffectedEntitiesPlotList(event);
		for(Plot plot:affectedEntitesPlots) {
			if (!PlotPermissionUtil.canPlayerAffectPlot(player, plot, Flag.SPLASH_POTION_MEMBER, Flag.SPLASH_POTION_STRANGER)){
				return false;
			}
		}
		return true;
	}
	private boolean canBlockSplashPotion(final PotionSplashEvent event) {
		BlockProjectileSource blockProjectileSource = (BlockProjectileSource) event.getPotion().getShooter();
		Block block = blockProjectileSource.getBlock();
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());

		return canPlotSplashPotion(blockPlot, event);
	}
	private boolean canLivingEntitySplashPotion(final PotionSplashEvent event) {
		LivingEntity livingEntity = (LivingEntity) event.getPotion().getShooter();
		Plot plot = plotManager.getPlotByLocation(livingEntity.getLocation());

		return canPlotSplashPotion(plot, event);
	}
	private List<Plot> getAffectedEntitiesPlotList(final PotionSplashEvent event){
		List<Plot> plots = new ArrayList<>();
		Collection<LivingEntity> affectedEntities = event.getAffectedEntities();

		for(LivingEntity entity:affectedEntities){
			Plot plot = plotManager.getPlotByLocation(entity.getLocation());
			if(plot != null){
				if(!plots.contains(plot)) {
					plots.add(plot);
				}
			}
		}
		return plots;
	}
	private boolean canPlotSplashPotion(final Plot plot, final  PotionSplashEvent event){
		List<Plot> affectedEntitesPlots = getAffectedEntitiesPlotList(event);
		if(plot != null) {
			return PlotPermissionUtil.canPlotAffectPlots(plot, affectedEntitesPlots);
		} else {
			return affectedEntitesPlots.isEmpty();
		}
	}

	@EventHandler
	public void onLingeringPotionSplash(final LingeringPotionSplashEvent event){
		if(!canLingeringPotionBeSplashed(event)){
			event.setCancelled(true);
		}
	}
	private boolean canLingeringPotionBeSplashed(LingeringPotionSplashEvent event) {
		ProjectileSource shooter = event.getEntity().getShooter();
		if(shooter instanceof Player){
			return canPlayerSplashLingeringPotion(event);
		} else if(shooter instanceof BlockProjectileSource){
			return canBlockSplashLingeringPotion(event);
		} else if(shooter instanceof LivingEntity){
			return canLivingEntitySplashLingeringPotion(event);
		}
		return false;
	}
	private boolean canPlayerSplashLingeringPotion(LingeringPotionSplashEvent event) {
		ProjectileSource shooter = event.getEntity().getShooter();
		Player player = (Player) shooter;

		Plot plot = plotManager.getPlotByLocation(event.getHitBlock().getLocation());

		if(plot != null) {
			return PlotPermissionUtil.canPlayerAffectPlot(player, plot, Flag.SPLASH_POTION_MEMBER, Flag.SPLASH_POTION_STRANGER);
		} else {
			return true;
		}
	}
	private boolean canBlockSplashLingeringPotion(LingeringPotionSplashEvent event) {
		ProjectileSource shooter = event.getEntity().getShooter();
		BlockProjectileSource blockProjectileSource = (BlockProjectileSource) shooter;
		Block block = blockProjectileSource.getBlock();

		Location splashLocation = event.getHitBlock().getLocation();
		Plot plot = plotManager.getPlotByLocation(splashLocation);

		if(plot != null) {
			return PlotPermissionUtil.canBlockAffectPlot(block, plot);
		} else {
			return true;
		}
	}
	private boolean canLivingEntitySplashLingeringPotion(LingeringPotionSplashEvent event) {
		ProjectileSource shooter = event.getEntity().getShooter();
		LivingEntity livingEntity = (LivingEntity) shooter;
		Plot shooterPlot = plotManager.getPlotByLocation(livingEntity.getLocation());

		Location splashLocation = event.getHitBlock().getLocation();
		Plot plot = plotManager.getPlotByLocation(splashLocation);
		if(plot != null){
			if(shooterPlot != null) {
				return plot.hasTheSameOwnerAs(shooterPlot);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}
