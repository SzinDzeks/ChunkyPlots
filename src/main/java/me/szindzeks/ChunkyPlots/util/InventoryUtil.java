package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.manager.MessageManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryUtil {
	public static ItemStack createItemStack(final Material material, final int amount, final String name, final List<String> lore, final HashMap<Enchantment, Integer> enchantments, final boolean isUnbreakable){
		final ItemStack itemStack = new ItemStack(material, amount);
		final ItemMeta itemMeta = itemStack.getItemMeta();
		modifyItemMetaWithValues(itemMeta, name, lore, enchantments, isUnbreakable);
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	private static void modifyItemMetaWithValues(ItemMeta itemMeta, String name, List<String> lore, HashMap<Enchantment, Integer> enchantments, boolean isUnbreakable){
		itemMeta.setDisplayName(MessageManager.fixColors(name));
		itemMeta.setLore(MessageManager.fixColors(lore));
		for(Enchantment e:enchantments.keySet()){
			itemMeta.addEnchant(e, enchantments.get(e), true);
		}
		itemMeta.setUnbreakable(isUnbreakable);
	}
}
