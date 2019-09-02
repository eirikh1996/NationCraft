package io.github.eirikh1996.nationcraft.territory;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Territory implements Comparable<Territory> {
    private final int x, z;
    private final World world;

    public Territory(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;
    }
    public static Territory fromChunk(Chunk chunk){
        return new Territory(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @NotNull
    public World getWorld() {
        return world;
    }

    @NotNull
    public Chunk getChunk(){
        return world.getChunkAt(x,z);
    }

    public boolean isTerritory(@NotNull Chunk c){
        return c.getX() == x && c.getZ() == z && c.getWorld() == world;
    }

    public boolean matches(@NotNull Territory territory){
        return territory == this;
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
    public final Vector toVector(){
        return new Vector(x,0,z);
    }


    @Override
    public int compareTo(@NotNull Territory o) {
        int i = 0;
        i += (getX() - o.getX());
        i += (getZ() - o.getZ());
        i += (getWorld().getName().compareToIgnoreCase(o.getWorld().getName()));
        return i;
    }
}
