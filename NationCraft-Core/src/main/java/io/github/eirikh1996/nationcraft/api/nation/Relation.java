package io.github.eirikh1996.nationcraft.api.nation;

import java.util.HashMap;
import java.util.Map;

public enum Relation {
    NEUTRAL, ALLY, TRUCE, ENEMY, OWN, SAFEZONE, WARZONE, WILDERNESS;

    private static Map<String, Relation> BY_NAME = new HashMap<>();

    static {
        for (Relation rel : Relation.values()) {
            BY_NAME.put(rel.name(), rel);
        }
    }

    public static Relation getRelationIgnoreCase(String id) {
        return getRelation(id.toUpperCase());
    }

    public static Relation getRelation(String id) {
        return BY_NAME.get(id);
    }

    public String capitalizedName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
