package io.github.eirikh1996.nationcraft.core.territory;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class TownCenter {
    private final int x, z;
    @NotNull private final UUID world;
    @NotNull private final NCLocation teleportationPoint;

    public TownCenter(int x, int z, @NotNull UUID world, @NotNull NCLocation teleportationPoint) {
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

    public NCVector getCenterPoint() {
        return new NCVector(x * 15 - 7, 0, z * 15 - 7);
    }
    @NotNull
    public UUID getWorld() {
        return world;
    }

    @NotNull
    public NCLocation getTeleportationPoint() {
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
        return String.format("{x: %d, z: %d, world: %s, teleportationPoint: %s}", getX(), getZ(), getWorld().toString(), getTeleportationPoint().toString());
    }
}
