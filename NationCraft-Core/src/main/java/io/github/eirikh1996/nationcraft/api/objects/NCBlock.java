package io.github.eirikh1996.nationcraft.api.objects;

public class NCBlock {
    private final String type;
    private final NCLocation location;


    public NCBlock(String type, NCLocation location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public NCLocation getLocation() {
        return location;
    }
}
