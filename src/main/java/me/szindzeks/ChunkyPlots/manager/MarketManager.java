package me.szindzeks.ChunkyPlots.manager;

import java.util.HashMap;

public class MarketManager {
	private HashMap<String, Integer> listenings = new HashMap<>();

	public MarketManager(){ loadListeningsFromFile(); }
	protected void finalize(){ saveListeningsToFile(); }

	public void createListening(String plotID, int price){
		listenings.put(plotID, price);
	}

	public void deleteListening(String plotID){
		listenings.remove(plotID);
	}

	public HashMap<String, Integer> getListenings() { return listenings; }

	public int getListeningPrice(String plotID){
		return listenings.get(plotID);
	}

	private void loadListeningsFromFile(){ }
	private void saveListeningsToFile(){ }
}
