package me.szindzeks.ChunkyPlots.listener;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.util.Vector;

public class DispenseListener implements Listener {
	@EventHandler
	public void onDispense(BlockDispenseEvent event){
		int x = event.getVelocity().getBlockX();
		int y = event.getVelocity().getBlockY();
		int z = event.getVelocity().getBlockZ();
		World world = event.getBlock().getWorld();
		Block source = event.getBlock();
		Block destination = world.getBlockAt(x, y, z);
		Plot sourcePlot = ChunkyPlots.plugin.plotManager.getPlot(source.getChunk());
		Plot destinationPlot = ChunkyPlots.plugin.plotManager.getPlot(destination.getChunk());
		if(sourcePlot == null && destinationPlot != null) event.setCancelled(true);
		else if(sourcePlot != null && destination != null){
			if(!sourcePlot.getOwnerNickname().equals(destinationPlot.getOwnerNickname()))
				event.setCancelled(true);
		}
	}
}
