package me.szindzeks.ChunkyPlots.basic;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {
	final public List<UUID> plots = new ArrayList<>();
	String name;

	public Group(String name){
		this.name = name;
	}

	public String getName() { return name; }
}
