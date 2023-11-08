package me.szindzeks.ChunkyPlots.manager;

import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.MessageType;
import me.szindzeks.ChunkyPlots.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConfigManager {
	private String pluginPrefix;

	private Material plotItemMaterial;
	private String plotItemName;
	private List<String> plotItemLore;

	HashMap<Flag, Boolean> defaultFlags = new HashMap<>();
	HashMap<MessageType, String> messages = new HashMap<>();

	public ConfigManager(){
		loadConfig();
	}

	private void loadConfig() {
		FileConfiguration config = ChunkyPlots.plugin.config;

		pluginPrefix = ChatUtils.fixColors(config.getString("pluginPrefix"));
		loadPlotItem(config);
		loadDefaultFlags(config);
		loadMessagesFromConfig();
	}

	private void loadMessagesFromConfig() {
		loadMessageFromConfig(MessageType.PLOT_CREATED, "plotCreated");
		loadMessageFromConfig(MessageType.PLOT_DELETED, "plotDeleted");
		loadMessageFromConfig(MessageType.ENTERED_PLOT, "enteredPlot");
		loadMessageFromConfig(MessageType.LEFT_PLOT, "leftPlot");
		loadMessageFromConfig(MessageType.PLOT_ALREADY_EXISTS, "plotAlreadyExists");
		loadMessageFromConfig(MessageType.NULL_PLOT, "nullPlot");
		loadMessageFromConfig(MessageType.ADDED_MEMBER_TO_PLOT, "addedMemberToPlot");
		//loadMessage(MessageType.ADDED_MEMBER_TO_GROUP, "addedMemberToGroup");
		loadMessageFromConfig(MessageType.REMOVED_MEMBER_FROM_PLOT, "removedMemberFromPlot");
		//loadMessage(MessageType.REMOVED_MEMBER_FROM_GROUP, "removedMemberFromGroup");
		loadMessageFromConfig(MessageType.BLACKLIST_ADDED_TO_PLOT, "blacklistAddedToPlot");
		//loadMessage(MessageType.BLACKLIST_ADDED_TO_GROUP, "blacklistAddedToGroup");
		loadMessageFromConfig(MessageType.BLACKLIST_REMOVED_FROM_PLOT, "blacklistRemovedFromPlot");
		//loadMessage(MessageType.BLACKLIST_REMOVED_FROM_GROUP, "blacklistRemovedFromGroup");
		loadMessageFromConfig(MessageType.FLAG_SET_ON_PLOT, "flagSetOnPlot");
		//loadMessage(MessageType.FLAG_SET_ON_GROUP, "flagSetOnGroup");
		loadMessageFromConfig(MessageType.FLAG_VALUE_ON_PLOT, "flagValueOnPlot");
		//loadMessage(MessageType.FLAG_VALUE_ON_GROUP, "flagValueOnGroup");
		loadMessageFromConfig(MessageType.FLAG_VALUES_IN_GROUP_ARE_DIFFERENT, "flagValuesInGroupAreDifferent");
		loadMessageFromConfig(MessageType.UNKNOWN_FLAG, "unknownFlag");
		loadMessageFromConfig(MessageType.WRONG_FLAG_VALUE, "wrongFlagValue");
		loadMessageFromConfig(MessageType.NOT_OWNER, "notOwner");
		loadMessageFromConfig(MessageType.NOT_PERMITTED, "notPermitted");
		loadMessageFromConfig(MessageType.CANNOT_ADD_OWNER_TO_BLACKLIST, "cannotAddOwnerToBlacklist");
		loadMessageFromConfig(MessageType.CANNOT_ADD_OWNER_AS_MEMBER, "cannotAddOwnerAsMember");
		loadMessageFromConfig(MessageType.PLAYER_IS_ALREADY_BLACKLISTED, "playerIsAlreadyBlacklisted");
		loadMessageFromConfig(MessageType.PLAYER_IS_ALREADY_A_MEMBER, "playerIsAlreadyAMember");
		loadMessageFromConfig(MessageType.NULL_USER, "nullUser");
		loadMessageFromConfig(MessageType.NULL_GROUP, "nullGroup");
		loadMessageFromConfig(MessageType.GROUP_ALREADY_EXISTS, "groupAlreadyExists");
		loadMessageFromConfig(MessageType.GROUP_CREATE, "groupCreate");
		loadMessageFromConfig(MessageType.GROUP_DELETE, "groupDelete");
		loadMessageFromConfig(MessageType.PLOT_ADDED_TO_GROUP, "plotAddedToGroup");
		loadMessageFromConfig(MessageType.PLOT_REMOVED_FROM_GROUP, "plotRemovedFromGroup");
		loadMessageFromConfig(MessageType.CREATED_VISIT_POINT, "createdVisitPoint");
		loadMessageFromConfig(MessageType.DEFAULT_VISIT_POINT_DESCRIPTION, "defaultVisitPointDescription");
		loadMessageFromConfig(MessageType.NULL_VISIT_POINT, "nullVisitPoint");
		loadMessageFromConfig(MessageType.VISIT_POINT_CLOSED, "visitPointClosed");
		loadMessageFromConfig(MessageType.NOT_VISIT_POINT_OWNER, "notVisitPointOwner");
		loadMessageFromConfig(MessageType.VISIT_POINT_ALREADY_EXISTS, "visitPointAlreadyExists");
		loadMessageFromConfig(MessageType.TELEPORTING_TO_VISIT_POINT, "teleportingToVisitPoint");
		loadMessageFromConfig(MessageType.TELEPORTED_TO_VISIT_POINT, "teleportedToVisitPoint");
		loadMessageFromConfig(MessageType.TELEPORT_CANCELLED, "teleportCancelled");
		loadMessageFromConfig(MessageType.SENDER_NOT_PLAYER, "senderNotPlayer");
		loadMessageFromConfig(MessageType.DELETED_VISIT_POINT, "deletedVisitPoint");
		loadMessageFromConfig(MessageType.VISIT_POINT_NOT_SAFE, "visitPointNotSafe");
		loadMessageFromConfig(MessageType.CANNOT_DELETE_DEFAULT_GROUP, "cannotDeleteDefaultGroup");
		loadMessageFromConfig(MessageType.CANNOT_REMOVE_PLOT_FROM_DEFAULT_GROUP, "cannotRemovePlotFromDefaultGroup");
		loadMessageFromConfig(MessageType.CANNOT_ADD_PLOT_TO_DEFAULT_GROUP, "cannotAddPlotToDefaultGroup");
		loadMessageFromConfig(MessageType.PLOT_NOT_IN_GROUP, "plotNotInGroup");
		loadMessageFromConfig(MessageType.PLOT_ALREADY_IN_GROUP, "plotAlreadyInGroup");
	}

	private void loadMessageFromConfig(MessageType type, String messageName){
		ConfigurationSection messageConfig = ChunkyPlots.plugin.config.getConfigurationSection("messages");
		messages.put(type, ChatUtils.fixColors(pluginPrefix + " " + messageConfig.getString(messageName)));
	}

	private void loadDefaultFlags(FileConfiguration config) {
		Set<String> keyList = config.getConfigurationSection("defaultFlags").getKeys(false);
		for(String key:keyList){
			boolean value = config.getBoolean("defaultFlags." + key);
			for(Flag flag:Flag.values()) {
				if(flag.name().equals(key)) defaultFlags.put(flag, value);
			}
		}
		//TODO: Change message below to english
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
	public String getMessage(MessageType type){
		return messages.get(type);
	}

}
