package me.szindzeks.ChunkyPlots.manager;


import me.szindzeks.ChunkyPlots.ChunkyPlots;
import me.szindzeks.ChunkyPlots.basic.Flag;
import me.szindzeks.ChunkyPlots.basic.Group;
import me.szindzeks.ChunkyPlots.basic.Plot;
import me.szindzeks.ChunkyPlots.basic.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserManager {
	private List<User> users = new ArrayList<>();
	File userDirectory = new File(ChunkyPlots.plugin.getDataFolder() + "/users");

	public UserManager(){
		loadUsers();
	}

	private void loadUsers(){
		File[] files = userDirectory.listFiles();
		if(files != null) {
			for (File file : files) {
				if (file != null) {
					FileConfiguration userData = YamlConfiguration.loadConfiguration(file);
					String nickname = file.getName();
					boolean isBypassingRestrictions = userData.getBoolean("isBypassingRestrictions");
					boolean hasEntered = userData.getBoolean("hasEntered");
					boolean cooldown = userData.getBoolean("cooldown");
					boolean isTeleporting = userData.getBoolean("isTeleporting");
					ConfigurationSection groupSection = userData.getConfigurationSection("groups");
					Set<String> keys = groupSection.getKeys(false);
					List<Group> groups = new ArrayList<>();
					for (String groupName : keys) {
						List<String> plotUUIDs = groupSection.getStringList(groupName);
						List<UUID> plots = new ArrayList<>();
						for(String s:plotUUIDs){
							plots.add(UUID.fromString(s));
						}
						Group group = new Group(groupName);
						group.plots = plots;
						groups.add(group);
					}

					User user = new User(nickname);
					user.isBypassingRestrictions = isBypassingRestrictions;
					user.hasEntered = hasEntered;
					user.cooldown = cooldown;
					user.isTeleporting = isTeleporting;
					user.groups = groups;
					users.add(user);
				}
			}
		}
	}
	public void saveUsers(){
		for(User user:users) {
			saveUser(user);
		}
	}

	public void saveUser(User user) {
		try {
			File file = new File(ChunkyPlots.plugin.getDataFolder() + "/users/" + user.getNickname());
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
			fc.set("isTeleporting", user.isTeleporting);
			fc.set("isBypassingRestrictions", user.isBypassingRestrictions);
			fc.set("hasEntered", user.hasEntered);
			fc.set("cooldown", user.cooldown);
			ConfigurationSection groupSection = fc.createSection("groups");
			for(Group group:user.groups){
				List<String> plotUUIDs = new ArrayList<>();
				for(UUID plotUUID:group.plots) {
					plotUUIDs.add(plotUUID.toString());
				}
				groupSection.set(group.getName(), plotUUIDs);
			}
			fc.save(file);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private List<User> obtainOnlinePlayersProfiles(List<Player> playersOnline){
		List<User> users = new ArrayList<>();
		for(Player player:playersOnline){
			User user = getUser(player.getName());
			if(user == null) users.add(new User(player.getName()));
		}
		return users;
	}

	public User getUser(String nickname){
		for(User user:users)
			if(user.getNickname().equals(nickname)) return user;
		return null;
	}
	public void addUser(User user){
		users.add(user);
	}
	public void removeUser(User user){
		users.remove(user);
	}

	public List<User> getPlots(){
		return users;
	}
}
