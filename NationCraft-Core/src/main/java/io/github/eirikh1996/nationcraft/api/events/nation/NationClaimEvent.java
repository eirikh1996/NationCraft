package io.github.eirikh1996.nationcraft.api.events.nation;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import io.github.eirikh1996.nationcraft.core.nation.Nation;

import java.util.Collection;

public class NationClaimEvent extends NationPlayerEvent implements Cancellable {

    private Collection<Territory> newTerritory;
    private boolean cancelled = false;

    public NationClaimEvent(Nation nation, NCPlayer player, Collection<Territory> newTerritory) {
        super(nation, player, true);
        this.newTerritory = newTerritory;
    }

    public void setNewTerritory(Collection<Territory> newTerritory) {
        this.newTerritory = newTerritory;
    }

    public Collection<Territory> getNewTerritory() {
        return newTerritory;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
