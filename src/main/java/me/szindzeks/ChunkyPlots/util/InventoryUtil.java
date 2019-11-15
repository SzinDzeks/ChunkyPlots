package me.szindzeks.ChunkyPlots.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryUtil {
	public static ItemStack createItem(Material material, int amount, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments, boolean isUnbreakable){
		ItemStack item = new ItemStack(material, amount);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatUtils.fixColors(name));

		List<String> fixedLore = new ArrayList<>();
		for(String s:lore) fixedLore.add(ChatUtils.fixColors(s));
		itemMeta.setLore(fixedLore);

		for(Enchantment e:enchantments.keySet()) itemMeta.addEnchant(e, enchantments.get(e), true);

		item.setItemMeta(itemMeta);
		return item;
	}
}
