package io.github.eirikh1996.nationcraft.territory;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TownCenter {
    private final int x, z;
    @NotNull private final World world;
    @NotNull private final Location teleportationPoint;

    public TownCenter(int x, int z, @NotNull World world, @NotNull Location teleportationPoint) {
        this.x = x;
        this.z = z;
        this.world = world;
        this.teleportationPoint = teleportationPoint;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    public Chunk getChunk(){
        return world.getChunkAt(x, z);
    }
    public Vector getCenterPoint() {
        return getChunk().getBlock(7, 0, 7).getLocation().toVector();
    }
    @NotNull
    public World getWorld() {
        return world;
    }

    @NotNull
    public Location getTeleportationPoint() {
        return teleportationPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, world, teleportationPoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TownCenter))
            return false;
        TownCenter other = (TownCenter) obj;
        return getX() == other.getX() &&
                getZ() == other.getZ() &&
                getWorld().equals(other.getWorld()) &&
                getTeleportationPoint().equals(other.getTeleportationPoint());
    }

    public boolean equalsTerritory(Territory territory){
        return getWorld() == territory.getWorld() && getX() == territory.getX() && getZ() == territory.getZ();
    }

    @Override
    public String toString() {
        return String.format("{x: %d, z: %d, world: %s, teleportationPoint: %s}", getX(), getZ(), getWorld().getName(), getTeleportationPoint().toString());
    }
}
