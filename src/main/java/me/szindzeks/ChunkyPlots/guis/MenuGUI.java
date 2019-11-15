package me.szindzeks.ChunkyPlots.guis;

import me.szindzeks.ChunkyPlots.basic.GUI;
import me.szindzeks.ChunkyPlots.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuGUI extends GUI {
	private Inventory inventory;
	private GUI previousGUI;

	public MenuGUI(GUI previousGUI) {
		this.previousGUI = previousGUI;
		this.inventory = generateInventory();
	}

	private Inventory generateInventory() {
		ItemStack item1 = InventoryUtil.createItem(Material.IRON_DOOR, 1, "&6&lSprawdź działki innego gracza", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		ItemStack item2 = InventoryUtil.createItem(Material.MINECART, 1, "&a&lOdwiedzanie działek", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		ItemStack item3 = InventoryUtil.createItem(Material.DIAMOND_PICKAXE, 1, "&b&lZarządzaj działką, na której stoisz", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		ItemStack item4 = InventoryUtil.createItem(Material.COMPASS, 1, "&9&lZarządzaj pozostałymi działkami", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		ItemStack item5 = InventoryUtil.createItem(Material.ENDER_CHEST, 1, "&2&lZarządzaj grupami działek", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		ItemStack spacing = InventoryUtil.createItem(Material.WHITE_STAINED_GLASS_PANE, 1, " ", new ArrayList<String>(), new HashMap<Enchantment, Integer>(), false);
		Inventory inventory = Bukkit.getServer().createInventory(null, 27);
		for(int i = 0; i < inventory.getSize(); i++){
			inventory.setItem(i,spacing);
		}
		inventory.setItem(9, item1);
		inventory.setItem(11, item2);
		inventory.setItem(13, item3);
		inventory.setItem(15, item4);
		inventory.setItem(17, item5);
		return inventory;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory().equals(inventory)) event.setCancelled(true);
	}

	public Inventory getInventory(){ return inventory; }
}
