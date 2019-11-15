package me.szindzeks.ChunkyPlots.basic;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUI implements Listener {
	private Inventory inventory;
	private GUI previousGUI;

	public Inventory getInventory(){
		return inventory;
	}

	public abstract void onInventoryClick(InventoryClickEvent event);

}
