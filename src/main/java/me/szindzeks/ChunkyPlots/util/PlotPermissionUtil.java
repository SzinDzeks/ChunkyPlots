package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class PlotPermissionUtil {
	private static PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	public static boolean canPistonAffectBlocks(Block piston, List<Block> affectedBlocks){
		for(Block block:affectedBlocks) {
			if (!canPistonAffectBlock(piston, block)) return false;
		}
		return true;
	}
	public static boolean canPistonAffectBlock(Block piston, Block affectedBlock){
		Plot pistonPlot = plotManager.getPlotByChunk(piston.getChunk());
		Plot affectedPlot = plotManager.getPlotByChunk(affectedBlock.getChunk());
		if(affectedPlot == null){
			return true;
		} else if(pistonPlot == null && affectedBlock != null){
			return false;
		} else if(pistonPlot.hasTheSameOwnerAs(affectedPlot)) {
			return true;
		} else if(affectedPlot.getFlags().get(Flag.EXTERNAL_PISTON_PROTECTION) == false){
			return true;
		} else {
			return false;
		}
	}

	public static boolean canPlayerAffectPlot(Player player, Plot plot, Flag memberFlag, Flag strangerFlag){
		if(plot.isPlayerOwner(player)){
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
			if (plot.hasTheSameOwnerAs(blockPlot)) {
				return true;
			}
		}
		return false;
	}

	public static boolean canPlotAffectPlots(Plot plot, List<Plot> plots){
		for(Plot affectedPlot:plots){
			if(plot != null) {
				if (!plot.hasTheSameOwnerAs(affectedPlot)) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		return true;
	}
}
