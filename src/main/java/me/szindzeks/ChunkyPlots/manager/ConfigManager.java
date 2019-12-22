package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConfigManager {
	private String pluginPrefix;

	private Material plotItemMaterial;
	private String plotItemName;
	private List<String> plotItemLore;

	HashMap<Flag, Boolean> defaultFlags = new HashMap<Flag, Boolean>();
	HashMap<MessageType, String> messages = new HashMap<MessageType, String>();

	public ConfigManager(){
		loadConfig();
	}

	private void loadConfig() {
		FileConfiguration config = ChunkyPlots.plugin.config;

		pluginPrefix = ChatUtils.fixColors(config.getString("pluginPrefix"));
		loadPlotItem(config);
		loadDefaultFlags(config);
		loadMessages(config);
	}

	private void loadMessages(FileConfiguration config) {
		ConfigurationSection messageConfig = config.getConfigurationSection("messages");

		messages.put(MessageType.PLOT_CREATED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("plotCreated")));
		messages.put(MessageType.PLOT_DELETED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("plotDeleted")));
		messages.put(MessageType.ENTERED_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("enteredPlot")));
		messages.put(MessageType.LEFT_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("leftPlot")));
		messages.put(MessageType.PLOT_ALREADY_EXISTS, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("plotAlreadyExists")));
		messages.put(MessageType.NULL_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("nullPlot")));
		messages.put(MessageType.ADDED_MEMBER_TO_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("addedMemberToPlot")));
		//messages.put(MessageType.ADDED_MEMBER_TO_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("addedMemberToGroup")));
		messages.put(MessageType.REMOVED_MEMBER_FROM_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("removedMemberFromPlot")));
		//messages.put(MessageType.REMOVED_MEMBER_FROM_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("removedMemberFromGroup")));
		messages.put(MessageType.BLACKLIST_ADDED_TO_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("blacklistAddedToPlot")));
		//messages.put(MessageType.BLACKLIST_ADDED_TO_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("blacklistAddedToGroup")));
		messages.put(MessageType.BLACKLIST_REMOVED_FROM_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("blacklistRemovedFromPlot")));
		//messages.put(MessageType.BLACKLIST_REMOVED_FROM_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("blacklistRemovedFromGroup")));
		messages.put(MessageType.FLAG_SET_ON_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("flagSetOnPlot")));
		//messages.put(MessageType.FLAG_SET_ON_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("flagSetOnGroup")));
		messages.put(MessageType.FLAG_VALUE_ON_PLOT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("flagValueOnPlot")));
		//messages.put(MessageType.FLAG_VALUE_ON_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("flagValueOnGroup")));
		messages.put(MessageType.FLAG_VALUES_IN_GROUP_ARE_DIFFERENT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("flagValuesInGroupAreDifferent")));
		messages.put(MessageType.UNKNOWN_FLAG, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("unknownFlag")));
		messages.put(MessageType.WRONG_FLAG_VALUE, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("wrongFlagValue")));
		messages.put(MessageType.NOT_OWNER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("notOwner")));
		messages.put(MessageType.NOT_PERMITTED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("notPermitted")));
		messages.put(MessageType.CANNOT_ADD_OWNER_TO_BLACKLIST, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("cannotAddOwnerToBlacklist")));
		messages.put(MessageType.CANNOT_ADD_OWNER_AS_MEMBER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("cannotAddOwnerAsMember")));
		messages.put(MessageType.PLAYER_IS_ALREADY_BLACKLISTED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("playerIsAlreadyBlacklisted")));
		messages.put(MessageType.PLAYER_IS_ALREADY_A_MEMBER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("playerIsAlreadyAMember")));
		messages.put(MessageType.NULL_USER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("nullUser")));
		messages.put(MessageType.NULL_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("nullGroup")));
		messages.put(MessageType.GROUP_CREATE, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("groupCreate")));
		messages.put(MessageType.GROUP_DELETE, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("groupDelete")));
		messages.put(MessageType.PLOT_ADDED_TO_GROUP, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("plotAddedToGroup")));
		messages.put(MessageType.DEFAULT_VISIT_POINT_DESCRIPTION, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("defaultVisitPointDescription")));
		messages.put(MessageType.NULL_VISIT_POINT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("nullVisitPoint")));
		messages.put(MessageType.VISIT_POINT_CLOSED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("visitPointClosed")));
		messages.put(MessageType.NOT_VISIT_POINT_OWNER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("notVisitPointOwner")));
		messages.put(MessageType.TELEPORTING_TO_VISIT_POINT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("teleportingToVisitPoint")));
		messages.put(MessageType.TELEPORTED_TO_VISIT_POINT, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("teleportedToVisitPoint")));
		messages.put(MessageType.TELEPORT_CANCELLED, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("teleportCancelled")));
		messages.put(MessageType.SENDER_NOT_PLAYER, pluginPrefix + " " + ChatUtils.fixColors(messageConfig.getString("senderNotPlayer")));
	}
	private void loadDefaultFlags(FileConfiguration config) {
		Set<String> keyList = config.getConfigurationSection("defaultFlags").getKeys(false);
		for(String key:keyList){
			boolean value = config.getBoolean("defaultFlags." + key);
			for(Flag flag:Flag.values()) {
				if(flag.name().equals(key)) defaultFlags.put(flag, value);
			}
		}
		if(defaultFlags.size() != Flag.values().length)
			Bukkit.getLogger().warning("Nie dla wszystkich flag ustawiono domyślne wartości! ("+ defaultFlags.size() +" z " + Flag.values().length +")");
	}
	private void loadPlotItem(FileConfiguration config){
		plotItemMaterial = Material.getMaterial(config.getString("plotItem.material"));
		plotItemName = config.getString("plotItem.name");
		plotItemLore = config.getStringList("plotItem.lore");
	}

	public String getPluginPrefix() { return  pluginPrefix; }
	public Material getPlotItemMaterial() { return  plotItemMaterial; }
	public String getPlotItemName() { return  plotItemName; }
	public List<String> getPlotItemLore() { return  plotItemLore; }
	public HashMap<Flag, Boolean> getDefaultFlags(){ return defaultFlags; }
	public HashMap<MessageType, String> getMessages() { return messages; }

}
