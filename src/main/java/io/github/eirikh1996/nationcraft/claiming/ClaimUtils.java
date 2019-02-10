package io.github.eirikh1996.nationcraft.claiming;

import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClaimUtils {
    public static Set<Chunk> claimCircularTerritory(Player player, int radius){
        Set<Chunk> returnList = new HashSet<>();
        Chunk pChunk = player.getLocation().getChunk();
        int maxX = pChunk.getX() + radius;
        int maxZ = pChunk.getZ() + radius;
        int minX = pChunk.getX() - radius;
        int minZ = pChunk.getZ() - radius;
        for (int x = minX ; x <= maxX ; x++){
            for (int z = minZ ; z <= maxZ ; z++){
                Vector distance = new Vector(x - pChunk.getX(), 0,z - pChunk.getZ());
                if (distance.length() <= radius){
                    Chunk foundChunk = player.getWorld().getChunkAt(x,z);
                    if (!foundChunk.isLoaded()){
                        foundChunk.load();
                    }
                    returnList.add(foundChunk);
                }
            }
        }
        return returnList;
    }
    public static Set<Chunk> claimSquareTerritory(Player player, int radius){
        Set<Chunk> returnList = new HashSet<>();
        Chunk pChunk = player.getLocation().getChunk();
        int maxX = pChunk.getX() + radius;
        int maxZ = pChunk.getZ() + radius;
        int minX = pChunk.getX() - radius;
        int minZ = pChunk.getZ() - radius;
        for (int x = minX ; x <= maxX ; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Chunk foundChunk = player.getWorld().getChunkAt(x,z);
                returnList.add(foundChunk);
            }
        }
        return returnList;
    }
    public static Set<Chunk> claimLineTerritory(Player player, int distance) {
        Set<Chunk> returnList = new HashSet<>();
        int minX;
        int minZ;
        int maxX;
        int maxZ;
        return returnList;
    }
}
