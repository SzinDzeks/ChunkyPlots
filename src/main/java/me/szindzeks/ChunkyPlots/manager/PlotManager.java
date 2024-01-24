package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.*;
import me.szindzeks.ChunkyPlots.util.InventoryUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

import static me.szindzeks.ChunkyPlots.manager.MessageManager.*;

public class PlotManager {
	private final List<Plot> plots = new ArrayList<Plot>();
	private ItemStack plotItem;
	File plotDirectory = new File(ChunkyPlots.plugin.getDataFolder() + "/plots");

	public PlotManager(){
		loadPlots();
		setupPlotItem();
	}

	private void setupPlotItem(){
		ConfigManager configManager = ChunkyPlots.plugin.configManager;
		plotItem = InventoryUtil.createItemStack(configManager.getPlotItemMaterial(), 1, configManager.getPlotItemName(), configManager.getPlotItemLore(), new HashMap<Enchantment, Integer>(), false);
	}
	private void loadPlots(){
		File[] files = plotDirectory.listFiles();
		if(files != null) {
			for (File file : files) {
				FileConfiguration plotData = YamlConfiguration.loadConfiguration(file);
				if (file != null) {
					String ownerNickname = plotData.getString("ownerNickname");
					int chunkX = plotData.getInt("chunkX");
					int chunkZ = plotData.getInt("chunkZ");
					String worldName = plotData.getString("worldName");
					List<String> members = plotData.getStringList("members");
					List<String> blacklist = plotData.getStringList("blacklist");;
					HashMap<String, Boolean> flagsToConvert = new HashMap<>();
					ConfigurationSection flagSection = plotData.getConfigurationSection("flags");
					for(String s:flagSection.getKeys(false)){
						flagsToConvert.put(s, flagSection.getBoolean(s));
					}
					HashMap<Flag, Boolean> flags = new HashMap<>();
					for(String s:flagsToConvert.keySet()){
						flags.put(Flag.valueOf(s),flagsToConvert.get(s));
					}

					Plot plot = new Plot(ownerNickname, chunkX, chunkZ, worldName, UUID.fromString(file.getName()));
					plot.blacklist = blacklist;
					plot.members = members;
					plot.blacklist = blacklist;
					plot.flags = flags;
					plots.add(plot);
				}
			}
		}
	}
	public void savePlots(){
		for(Plot plot:plots) {
			savePlot(plot);
		}
	}
	public void savePlot(Plot plot) {
		try {
			File file = new File(ChunkyPlots.plugin.getDataFolder() + "/plots/" + plot.getUUID());
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
			fc.set("ownerNickname", plot.getOwnerNickname());
			fc.set("chunkX", plot.getChunkX());
			fc.set("chunkZ", plot.getChunkZ());
			fc.set("worldName", plot.getWorldName());
			fc.set("members", plot.getMembers());
			fc.set("blacklist", plot.getBlacklist());
			ConfigurationSection flagSection = fc.createSection("flags");
			for(Flag flag:plot.getFlags().keySet()){
				flagSection.set(flag.name(), plot.getFlags().get(flag));
			}
			fc.save(file);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public ItemStack getPlotItem() {
		return plotItem;
	}
	public List<Plot> getPlots(){
		return plots;
	}

	public Plot getPlotByBlock(Block block){
		return getPlotByChunk(block.getChunk());
	}
	public Plot getPlotByChunk(Chunk chunk){
		for(Plot plot:plots) {
			if (plot.getChunkX() == chunk.getX() && plot.getChunkZ() == chunk.getZ()) return plot;
		}
		return null;
	}
	public Plot getPlotByUUID(UUID uuid){
		for(Plot plot:plots) {
			if (plot.getUUID().equals(uuid)) return plot;
		}
		return null;
	}
	public Plot getPlotByCoordinates(String x, String z, String worldName){
		int parsedX = Integer.parseInt(x);
		int parsedZ = Integer.parseInt(z);
		return getPlotByCoordinates(parsedX, parsedZ, worldName);
	}
	public Plot getPlotByCoordinates(int x, int z, String worldName){
		for(Plot plot:plots) {
			if (
					plot.getChunkX() == x &&
							plot.getChunkZ() == z &&
							plot.getWorldName().equals(worldName)
			) return plot;
		}
		return null;
	}
	public Plot getPlotByLocation(Location location){
		Chunk chunk = location.getChunk();
		return getPlotByChunk(chunk);
	}

	public void disposePlot(Plot plot){
		plots.remove(plot);
		File f = new File(plotDirectory.getPath() + "/" + plot.getUUID());
		f.delete();
	}
	public boolean isInsidePlot(Location location){
		for(Plot plot:plots) {
			if (location.getChunk().getX() == plot.getChunkX() && location.getChunk().getZ() == plot.getChunkZ())
				return true;
		}
		return false;
	}

	public void claimPlot(Player player, Block block){
		ConfigManager configManager = ChunkyPlots.plugin.configManager;
		Chunk chunk = block.getChunk();
		String plotID = chunk.getX() + ";" + chunk.getZ();
		if(getPlotByChunk(chunk) == null){
			Plot plot = new Plot(player, chunk);
			User user = ChunkyPlots.plugin.userManager.getUser(player.getName());

			assignPlotToUserDefaultGroup(plot, user);
			plots.add(plot);
			savePlot(plot);
			sendMessage(player, configManager.getMessage(MessageType.PLOT_CREATED).replace("{plotID}", plotID));
		} else sendMessage(player, configManager.getMessage(MessageType.PLOT_ALREADY_EXISTS).replace("{plotID}", plotID));
	}

	private void assignPlotToUserDefaultGroup(Plot plot, User user){
		for(Group group:user.groups){
			if(group.getName().equals("all")) group.plots.add(plot.getUUID());
		}
	}
}
