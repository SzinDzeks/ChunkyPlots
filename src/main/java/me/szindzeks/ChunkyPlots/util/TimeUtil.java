package me.szindzeks.ChunkyPlots.util;

import org.bukkit.Bukkit;

public class TimeUtil {
	public static void displayProcessingTime(long executionStartTime, String eventName){
		long executionStopTime = System.nanoTime();
		long executionDuration = executionStartTime - executionStopTime;
		double executionPercentageDuration = Math.round(executionDuration / 500000);
		Bukkit.getServer().getLogger().info(eventName + " processing time: " + executionDuration + "ns");
		Bukkit.getServer().getLogger().info(eventName + " processing time: " + executionPercentageDuration + "%");
	}
}
