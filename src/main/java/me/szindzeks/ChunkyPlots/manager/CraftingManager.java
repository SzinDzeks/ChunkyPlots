package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import me.szindzeks.ChunkyPlots.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {
	public static ItemStack plotBlock;
	private ConfigManager configManager = ChunkyPlots.plugin.configManager;

	public CraftingManager(){
		createPlotItem();
		loadShapedRecipes(createShapedRecipes());
	}

	private void createPlotItem() {
		String name = configManager.getPlotItemName();
		List<String> lore = configManager.getPlotItemLore();
		HashMap<Enchantment, Integer> enchantments = new HashMap<>();
		enchantments.put(Enchantment.DURABILITY, 1);

		plotBlock = InventoryUtil.createItemStack(Material.NOTE_BLOCK, 1, name, lore, enchantments, false);
	}

	private void loadShapedRecipes(List<ShapedRecipe> recipes){
		for(ShapedRecipe recipe:recipes){
			Bukkit.addRecipe(recipe);
		}
	}

	private List<ShapedRecipe> createShapedRecipes(){
		List<ShapedRecipe> list = new ArrayList<>();
		list.add(createPlotBlockRecipe());
		return list;
	}

	private ShapedRecipe createPlotBlockRecipe(){
		NamespacedKey key = new NamespacedKey(ChunkyPlots.plugin, "plot_block");
		ShapedRecipe recipe = new ShapedRecipe(key, plotBlock);
		recipe.shape("fff", "fef", "fgf");
		recipe.setIngredient('f', Material.OAK_FENCE);
		recipe.setIngredient('g', Material.OAK_FENCE_GATE);
		recipe.setIngredient('e', Material.EMERALD);

		return recipe;
	}
}
