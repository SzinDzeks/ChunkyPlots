package me.szindzeks.ChunkyPlots.manager;


import me.szindzeks.ChunkyPlots.basic.User;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserManager {
	private final List<User> users = new ArrayList<User>();

	public UserManager(){
		List<Player> playersOnlne = (List<Player>) Bukkit.getServer().getOnlinePlayers();
		for(Player player:playersOnlne){
			User user = getUser(player.getName());
			if(user == null) addUser(new User(player.getName()));
		}
	}

	public List<User> getPlots(){ return users; }
	public User getUser(String nickname){
		for(User user:users)
			if(user.getNickname().equals(nickname)) return user;
		return null;
	}

	public void addUser(User user){ users.add(user); }
	public void removeUser(User user){ users.remove(user); }
}
