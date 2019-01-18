package io.github.eirikh1996.nationcraft.nation;

import com.google.common.collect.Maps;

import java.util.Map;

public enum Ranks {
	RECRUIT, MEMBER, OFFICER, OFFICIAL, LEADER;
	


	public static Ranks getRank(String name){
		Ranks returnRank = null;
		if (name.equalsIgnoreCase(RECRUIT.name())){
			returnRank = RECRUIT;
		} else if (name.equalsIgnoreCase(MEMBER.name())){
			returnRank = MEMBER;
		} else if (name.equalsIgnoreCase(OFFICER.name())){
			returnRank = OFFICER;
		} else if (name.equalsIgnoreCase(OFFICIAL.name())){
			returnRank = OFFICIAL;
		} else if (name.equalsIgnoreCase(LEADER.name())){
			returnRank = LEADER;
		}
		return returnRank;
	}
}
