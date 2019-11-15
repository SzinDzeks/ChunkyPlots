package me.szindzeks.ChunkyPlots.manager;

import com.sun.istack.internal.NotNull;
import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Group;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import me.szindzeks.ChunkyPlots.manager.ConfigManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlotManager {
	private final List<Plot> plots = new ArrayList<Plot>();
	private ItemStack plotItem = new ItemStack(ChunkyPlots.plugin.configManager.getPlotItemMaterial());

	public void PlotManager(){
		setupPlotItem();
		loadPlots();
	}

	private void setupPlotItem(){
		ConfigManager configManager = ChunkyPlots.plugin.configManager;
		ItemMeta itemMeta = plotItem.getItemMeta();

		itemMeta.setDisplayName(configManager.getPlotItemName());
		itemMeta.setLore(configManager.getPlotItemLore());
		plotItem.setItemMeta(itemMeta);
	}
	private void loadPlots() {

	}

	public ItemStack getPlotItem() { return plotItem; }
	public List<Plot> getPlots(){ return plots; }
	public Plot getPlot(Chunk chunk){
		for(Plot plot:plots) {
			if (plot.getChunkX() == chunk.getX() && plot.getChunkZ() == chunk.getZ()) return plot;
		}
		return null;
	}
	public Plot getPlot(UUID uuid){
		for(Plot plot:plots) {
			if (plot.getUUID().equals(uuid)) return plot;
		}
		return null;
	}
	public Plot getPlot(String x, String z, String worldName){
		int parsedX = Integer.parseInt(x);
		int parsedZ = Integer.parseInt(z);
		for(Plot plot:plots) {
			if (
				plot.getChunkX() == parsedX &&
				plot.getChunkZ() == parsedZ &&
				plot.getWorldName().equals(worldName)
			) return plot;
		}
		return null;
	}
	public Plot getPlot(int x, int z, String worldName){
		for(Plot plot:plots) {
			if (
				plot.getChunkX() == x &&
				plot.getChunkZ() == z &&
				plot.getWorldName().equals(worldName)
			) return plot;
		}
		return null;
	}

	public void addPlot(Plot plot){ plots.add(plot); }
	public void removePlot(Plot plot){ plots.remove(plot); }

	public boolean isInsidePlot(Location location){
		for(Plot plot:plots) {
			if (location.getChunk().getX() == plot.getChunkX() && location.getChunk().getZ() == plot.getChunkZ())
				return true;
		}
		return false;
	}

	public void claimPlot(Player player, Block block){
		HashMap<MessageType, String> messages = ChunkyPlots.plugin.configManager.getMessages();
		Chunk chunk = block.getChunk();
		String plotID = chunk.getX() + ";" + chunk.getZ();
		if(getPlot(chunk) == null){
			Plot plot = new Plot(player, chunk);

			User user = ChunkyPlots.plugin.userManager.getUser(player.getName());
			for(Group group:user.groups){
				if(group.getName().equals("all")) group.plots.add(plot.getUUID());
			}
			addPlot(plot);
			player.sendMessage(messages.get(MessageType.CREATED_PLOT).replace("%plotID%", plotID));
		} else player.sendMessage(messages.get(MessageType.PLOT_ALREADY_EXISTS).replace("%plotID%", plotID));
	}
}
