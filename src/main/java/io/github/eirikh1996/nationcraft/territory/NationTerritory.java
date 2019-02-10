package io.github.eirikh1996.nationcraft.territory;

import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.World;

public class NationTerritory extends Territory {
    private final Nation nation;
    public NationTerritory(World world, Nation nation, int x, int z) {
        super(world, x, z);
        this.nation = nation;
    }
}
