package me.szindzeks.ChunkyPlots.util;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.manager.PlotManager;
import org.bukkit.block.Block;

import java.util.List;

public class PistonUtil {
	private static PlotManager plotManager = ChunkyPlots.plugin.plotManager;

	public static boolean hasPistonPermissionToAffectBlocks(Block piston, List<Block> affectedBlocks){
		for(Block block:affectedBlocks) {
			if (!pistonCanAffectBlock(piston, block)) return false;
		}
		return true;
	}

	public static boolean pistonCanAffectBlock(Block piston, Block affectedBlock){
		Plot pistonPlot = plotManager.getPlotByChunk(piston.getChunk());
		Plot affectedBlockPlot = plotManager.getPlotByChunk(affectedBlock.getChunk());
		if(affectedBlockPlot == null) return true;
		else if(affectedBlockPlot.getFlags().get(Flag.EXTERNAL_PISTON_PROTECTION) == false) return true;
		else return pistonPlot.hasTheSameOwnerAs(affectedBlockPlot);
	}
}
