package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class PlotPermissionUtil {
	private static final PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	public static boolean canPlayerAffectPlot(Player player, Plot plot, Flag memberFlag, Flag strangerFlag){
		if (plot == null){
			return true;
		} else if(plot.isPlayerOwner(player)){
			return true;
		} else if(plot.isPlayerMember(player)){
			return canMemberAffectPlot(plot, memberFlag);
		} else {
			return canStrangerAffectPlot(plot, strangerFlag);
		}
	}

	public static boolean canMemberAffectPlot(Plot plot, Flag memberFlag) {
		if(memberFlag == null) {
			return true;
		} else {
			return plot.flags.get(memberFlag);
		}
	}

	public static boolean canStrangerAffectPlot(Plot plot, Flag strangerFlag) {
		if(strangerFlag == null) {
			return false;
		} else {
			return plot.flags.get(strangerFlag);
		}
	}

	public static boolean canBlockAffectPlot(Block block, Plot plot){
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
		if(blockPlot != null) {
			return plot.hasTheSameOwnerAs(blockPlot);
		}
		return false;
	}

	public static boolean canBlockAffectBlock(Block block, Block block2){
		Plot blockPlot = plotManager.getPlotByChunk(block.getChunk());
		Plot block2Plot = plotManager.getPlotByChunk(block2.getChunk());
		if(blockPlot != null) {
			return blockPlot.hasTheSameOwnerAs(block2Plot);
		}
		return false;
	}

	public static boolean canPlotAffectPlots(Plot plot, List<Plot> plots){
		for(Plot affectedPlot:plots){
			if(plot != null) {
				return plot.hasTheSameOwnerAs(affectedPlot);
			} else {
				return false;
			}
		}
		return true;
	}

}
