package io.github.eirikh1996.nationcraft.api.objects;

import java.util.UUID;

public class NCLocation {
    private final UUID world;
    private final double x, y, z;
    private final float pitch, yaw;
    public NCLocation(UUID world, double x, double y, double z) {
        this(world, x, y, z, 0f, 0f);
    }

    public NCLocation(UUID world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public UUID getWorld() {
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
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
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
}
