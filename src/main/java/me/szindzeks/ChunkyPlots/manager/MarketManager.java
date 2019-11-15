package me.szindzeks.ChunkyPlots.manager;

import com.sun.istack.internal.NotNull;
import me.szindzeks.ChunkyPlots.basic.Plot;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;

import java.util.ArrayList;
import java.util.HashMap;

public class MarketManager {
	private HashMap<String, Integer> listenings = new HashMap<>();

	public MarketManager(){ loadListeningsFromFile(); }
	public void finalize(){ saveListeningsToFile(); }

	@NotNull
	public void createListening(String plotID, int price){
		listenings.put(plotID, price);
	}

	public void deleteListening(String plotID){
		listenings.remove(plotID);
	}

	public HashMap<String, Integer> getListenings() { return listenings; }

	@NotNull
	public int getListeningPrice(String plotID){
		return listenings.get(plotID);
	}

	private void loadListeningsFromFile(){ }
	private void saveListeningsToFile(){ }
}
