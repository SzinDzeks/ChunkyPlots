package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
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

	public CraftingManager(){
		loadShapedRecipes(createShapedRecipes());
	}

	private void loadShapedRecipes(List<ShapedRecipe> recipes){
		for(ShapedRecipe recipe:recipes) Bukkit.addRecipe(recipe);
	}

	private List<ShapedRecipe> createShapedRecipes(){
		List<ShapedRecipe> list = new ArrayList<>();
		list.add(createPlotBlockRecipe());
		return list;
	}

	private ShapedRecipe createPlotBlockRecipe(){
		List<String> lore = new ArrayList<>(Arrays.asList(
				"&7Ten blok umożliwi ci zajęcie chunku tak,",
				"&7aby nikt niezaufany nie mógł ci zniszczyć",
				"&7działki.",
				"&eAby zobaczyć granicę chunku wciśnij &6F3 + G"
		));
		HashMap<Enchantment, Integer> enchantments = new HashMap<>();
		enchantments.put(Enchantment.DURABILITY, 10);
		plotBlock = InventoryUtil.createItem(Material.NOTE_BLOCK, 1, "&6&lBLOK DZIAŁKI", lore, enchantments, false);

		NamespacedKey key = new NamespacedKey(ChunkyPlots.plugin, "plot_block");
		ShapedRecipe recipe = new ShapedRecipe(key, plotBlock);
		recipe.shape("ppp", "pep", "pgp");
		recipe.setIngredient('p', Material.OAK_FENCE);
		recipe.setIngredient('g', Material.OAK_FENCE_GATE);
		recipe.setIngredient('e', Material.EMERALD_BLOCK);

		return recipe;
	}
}
