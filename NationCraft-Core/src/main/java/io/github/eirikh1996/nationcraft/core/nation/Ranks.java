package io.github.eirikh1996.nationcraft.core.nation;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public enum Ranks {
	RECRUIT(10000), MEMBER(1000), OFFICER(100), OFFICIAL(10), LEADER(1);

	public static Map<String, Ranks> BY_NAME = new HashMap<>();
	private final int priority;

	static {
		for (Ranks r : values()) {
			BY_NAME.put(r.name(), r);
		}
	}

	Ranks(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public static Ranks getRank(String name){
		return BY_NAME.get(name.toUpperCase());
	}

	public static Ranks getRank(String name, Ranks def) {
		return BY_NAME.getOrDefault(name.toUpperCase(), def);
	}
}
