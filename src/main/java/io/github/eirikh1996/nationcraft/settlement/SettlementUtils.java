package io.github.eirikh1996.nationcraft.settlement;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class SettlementUtils {
    public static float exposurePercent(Settlement settlement){
        float exposure = 0.0F;
        World world = settlement.getWorld();
        Set<Chunk> surroundings = new HashSet<>();
        Set<Chunk> notNationTerr = new HashSet<>();
        for (Chunk c : settlement.getTerritory()){
            Chunk ec = world.getChunkAt(c.getX() - 1,c.getZ());
            Chunk wc = world.getChunkAt(c.getX() + 1,c.getZ());
            Chunk nc = world.getChunkAt(c.getX() ,c.getZ() - 1);
            Chunk sc = world.getChunkAt(c.getX(),c.getZ() + 1);
            if (!settlement.getTerritory().contains(ec)){
                surroundings.add(ec);
            }
            if (!settlement.getTerritory().contains(wc)){
                surroundings.add(wc);
            }
            if (!settlement.getTerritory().contains(nc)){
                surroundings.add(nc);
            }
            if (!settlement.getTerritory().contains(sc)){
                surroundings.add(sc);
            }
        }
        for (Chunk c : surroundings){
            Nation foundN = NationManager.getInstance().getNationAt(c);
            if (foundN == settlement.getNation()){
                continue;
            }
            notNationTerr.add(c);
        }
        exposure = ((float) notNationTerr.size() / (float) surroundings.size()) * 100.0F;
        return exposure;
    }
}
