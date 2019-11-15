package me.szindzeks.ChunkyPlots.basic;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class Plot {
	private String ownerNickname;
	public final List<String> blacklist = new ArrayList<String>();
	public final List<String> members = new ArrayList<String>();
	private HashMap<Flag, Boolean> flags = new HashMap<Flag, Boolean>(ChunkyPlots.plugin.configManager.getDefaultFlags());
	private int chunkX;
	private int chunkZ;
	private String worldName;
	private UUID uuid = UUID.randomUUID();

	public Plot(Player player, Chunk chunk){
		this.ownerNickname = player.getName();
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
		this.worldName = chunk.getWorld().getName();
	}

	public void setFlag(Flag flag, boolean value){ flags.put(flag, value); }

	public String getOwnerNickname(){ return ownerNickname; }
	public HashMap<Flag, Boolean> getFlags() { return flags; }
	public int getChunkX() { return chunkX; }
	public int getChunkZ() { return chunkZ; }
	public String getWorldName(){ return worldName; }
	public UUID getUUID(){ return uuid; }
	public String getID(){ return chunkX + ";" + chunkZ; }

	public boolean isInside(Location location){
		return location.getChunk().getX() == chunkX && location.getChunk().getZ() == chunkZ;
	}
}
