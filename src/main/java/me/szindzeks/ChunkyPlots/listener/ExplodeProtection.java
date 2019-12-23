package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import me.szindzeks.ChunkyPlots.util.PlotPermissionUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ExplodeProtection implements Listener {
	private final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	private final HashMap<UUID, Player> witherSummoners = new HashMap<>();
	private Player lastWitherBlockPlacer;

	private final HashMap<UUID, Block> tntPrimedDispensers = new HashMap<>();
	private final HashMap<Location, Block> tntDispenseBlockAndLocation = new HashMap<>();

	@EventHandler
	public void onEntityExplode(final EntityExplodeEvent event){
		if(!canEntityExplodeBlocks(event.getEntity(), event.blockList())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntitySpawn(final EntitySpawnEvent event){
		if(event.getEntity() instanceof Wither){
			witherSummoners.put(event.getEntity().getUniqueId(), lastWitherBlockPlacer);
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event){
		Material blockMaterial = event.getBlockPlaced().getType();
		if(blockMaterial.equals(Material.SOUL_SAND) || blockMaterial.equals(Material.WITHER_SKELETON_SKULL)){
			lastWitherBlockPlacer = event.getPlayer();
		}
	}

	private boolean canEntityExplodeBlocks(Entity entity, List<Block> blockList) {
		for (final Block block:blockList) {
			if(!canEntityExplodeBlock(entity, block)){
				return false;
			}
		}
		return true;
	}

	private boolean canEntityExplodeBlock(Entity entity, Block block) {
		if(entity instanceof Wither){
			Wither wither = (Wither) entity;
			return canWitherExplodeBlock(wither, block);
		} else if(entity instanceof WitherSkull){
			WitherSkull witherSkull = (WitherSkull) entity;
			return canWitherSkullExplodeBlock(witherSkull, block);
		} else if(entity instanceof TNTPrimed){
			TNTPrimed tntPrimed = (TNTPrimed) entity;
			return canTNTPrimedExplodeBlock(tntPrimed, block);
		} else if(entity instanceof ExplosiveMinecart){
			ExplosiveMinecart explosiveMinecart = (ExplosiveMinecart) entity;
			return canExplosiveMinecraftExplodeBlock(explosiveMinecart, block);
		} else if(entity instanceof EnderCrystal){
			EnderCrystal enderCrystal = (EnderCrystal) entity;
			return canEnderCrystalExplodeBlock(enderCrystal, block);
		} else {
			return false;
		}
	}

	private boolean canWitherExplodeBlock(Wither wither, Block block) {
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
		UUID uuid = wither.getUniqueId();
		Player summoner = witherSummoners.get(uuid);

		if(summoner != null){
			return PlotPermissionUtil.canPlayerAffectPlot(summoner, blockPlot, Flag.BREAK_MEMBER, Flag.BREAK_STRANGER);
		} else {
			return false;
		}
	}

	private boolean canWitherSkullExplodeBlock(WitherSkull witherSkull, Block block) {
		if(witherSkull.getShooter() instanceof Wither) {
			Wither wither = (Wither) witherSkull.getShooter();
			return canWitherExplodeBlock(wither, block);
		} else {
			return false;
		}
	}

	private boolean canTNTPrimedExplodeBlock(TNTPrimed tntPrimed, Block block) {
		Entity tntSource = tntPrimed.getSource();
		if(tntSource != null) {
			if (tntSource.isValid()) {
				Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
				if (tntSource instanceof Player) {
					Player player = (Player) tntSource;
					return PlotPermissionUtil.canPlayerAffectPlot(player, blockPlot, Flag.EXPLODE_MEMBER, Flag.EXPLODE_STRANGER);
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean canExplosiveMinecraftExplodeBlock(ExplosiveMinecart explosiveMinecart, Block block) {
		return false;
	}

	private boolean canEnderCrystalExplodeBlock(EnderCrystal enderCrystal, Block block) {
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
		Plot crystalPlot = plotManager.getPlotByChunk(enderCrystal.getLocation().getChunk());
		if(blockPlot.getOwnerNickname().equals(crystalPlot.getOwnerNickname())){
			return true;
		} else {
			return false;
		}
	}
}
