package io.github.eirikh1996.nationcraft.core.territory;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import io.github.eirikh1996.nationcraft.core.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public final class Territory implements Comparable<Territory> {
    private final int x, z;
    private final UUID world;

    public Territory(UUID world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Gets the x value of the territory chunk
     * @return x chunk coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the z value of the territory chunk
     * @return z chunk coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the world this territory chunk is located in
     * @return the world this territory chunk is located in
     */
    @NotNull
    public UUID getWorld() {
        return world;
    }

    public boolean contains(UUID world, int x, int z) {
        return this.world == world && x >> 4 == this.x && z >> 4 == this.z;
    }

    public boolean contains(NCLocation loc) {
        return contains(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
    }

    public boolean matches(@NotNull Territory territory){
        return territory == this;
    }

    /**
     * Returns a territory chunk at the relative offset of this territory chunk
     * @param dx distance in x direction
     * @param dz distance in z direction
     * @return territory chunk at given relative offset
     */
    public Territory getRelative(int dx, int dz){
        return new Territory(world, x + dx, z + dz);
    }

    public Territory getRelative(Direction direction){
        if (direction == null){
            throw new IllegalArgumentException("Direction parameter cannot be null!");
        }
        switch (direction){
            case NORTH:
                return new Territory(world, x, z - 1);
            case NORTH_WEST:
                return new Territory(world, x - 1, z - 1);
            case WEST:
                return new Territory(world, x - 1, z);
            case SOUTH_WEST:
                return new Territory(world, x - 1, z + 1);
            case SOUTH:
                return new Territory(world, x, z + 1);
            case SOUTH_EAST:
                return new Territory(world, x + 1, z + 1);
            case EAST:
                return new Territory(world, x + 1, z);
            case NORTH_EAST:
                return new Territory(world, x + 1, z - 1);
        }
        return null;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Territory)){
            return false;
        }
        Territory other = (Territory) obj;

        return other.getWorld() == getWorld() && other.getX() == getX() && other.getZ() == getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(world,x,z);
    }

    @NotNull
    public final NCVector toVector(){
        return new NCVector(x,0,z);
    }

    @Override
    public String toString() {
        return "[" + world.toString() + ", " + x + ", " + z + "]";
    }

    @Override
    public int compareTo(@NotNull Territory o) {
        int i = 0;
        i += (getX() - o.getX());
        i += (getZ() - o.getZ());
        i += (getWorld().compareTo(o.getWorld()));
        return i;
    }
}
