package io.github.eirikh1996.nationcraft.territory;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public abstract class Territory {
    private final int x, z;
    private final World world;

    public Territory(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public boolean isTerritory(Chunk c){
        return c.getX() == x && c.getZ() == z && c.getWorld() == world;
    }


}
