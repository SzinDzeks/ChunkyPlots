package me.szindzeks.ChunkyPlots.basic;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String nickname;
	public boolean isBypassingRestrictions, hasEntered, cooldown, isTeleporting = false;
	public List<Group> groups = new ArrayList<>();

	public User(String nickname){
		this.nickname = nickname;
		groups.add(new Group("all"));
	}
	public String getNickname(){ return nickname; }
	public Group getGroupByName(String groupName){
		for(Group group:groups){
			if(group.name.equals(groupName)) return group;
		}
		return null;
	}
}
