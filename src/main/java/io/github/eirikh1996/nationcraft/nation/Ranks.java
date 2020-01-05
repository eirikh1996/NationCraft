package io.github.eirikh1996.nationcraft.nation;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public enum Ranks {
	RECRUIT, MEMBER, OFFICER, OFFICIAL, LEADER;

	public static Map<String, Ranks> BY_NAME = new HashMap<>();

	static {
		for (Ranks r : values()) {
			BY_NAME.put(r.name(), r);
		}
	}


	public static Ranks getRank(String name){
		return BY_NAME.get(name.toUpperCase());
	}

	public static Ranks getRank(String name, Ranks def) {
		return BY_NAME.getOrDefault(name.toUpperCase(), def);
	}
}
