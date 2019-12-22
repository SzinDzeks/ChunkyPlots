package me.szindzeks.ChunkyPlots.util;

import org.bukkit.Bukkit;

public class TimeUtil {
	public static void displayEventExecutionTime(String eventName, long executionStartNanoTime){
		long executionStopNanoTime = System.nanoTime();
		long durationInNanoseconds = calculateExecutionDuration(executionStartNanoTime, executionStopNanoTime);
		double durationAsPercentageOfSecond = calculateDurationAsPercentageOfSecond(durationInNanoseconds);

		displayExecutionDurationInNanoseconds(eventName, durationInNanoseconds);
		displayExecutionDurationAsPercentageOfSecond(eventName, durationAsPercentageOfSecond);
	}
	private static long calculateExecutionDuration(long executionStartNanoTime, long executionStopNanoTime){
		return executionStopNanoTime - executionStartNanoTime;
	}
	private static double calculateDurationAsPercentageOfSecond(long executionDurationInNanoseconds){
		return Math.round(executionDurationInNanoseconds / 500000);
	}
	private static void displayExecutionDurationInNanoseconds(String eventName, long executionDurationInNanoseconds){
		Bukkit.getServer().getLogger().info(eventName + " processing time: " + executionDurationInNanoseconds + "ns");
	}
	private static void displayExecutionDurationAsPercentageOfSecond(String eventName, double executionDurationAsPercentageOfSecond){
		Bukkit.getServer().getLogger().info(eventName + " processing time: " + executionDurationAsPercentageOfSecond + "% of 1 second");
	}
}
