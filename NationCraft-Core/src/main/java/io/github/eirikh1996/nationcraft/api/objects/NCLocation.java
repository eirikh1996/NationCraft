package io.github.eirikh1996.nationcraft.api.objects;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.territory.Territory;

public class NCLocation {
    private final String world;
    private final double x, y, z;
    private final float pitch, yaw;
    public NCLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0f, 0f);
    }

    public NCLocation(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public NCVector toVector() {
        return new NCVector(x, y, z);
    }

    public int getChunkX() {
        return getBlockX() >> 4;
    }

    public int getChunkZ() {
        return getBlockZ() >> 4;
    }

    public Territory getTerritory() {
        return new Territory(world, getChunkX(), getChunkZ());
    }

    public Nation getNation() {
        return getTerritory().getNation();
    }

    public Settlement getSettlement() {
        return getTerritory().getSettlement();
    }
}
