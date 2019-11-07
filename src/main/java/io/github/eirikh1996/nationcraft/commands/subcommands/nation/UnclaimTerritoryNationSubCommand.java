package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.events.nation.NationClaimTerritoryEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public final class UnclaimTerritoryNationSubCommand extends NationSubCommand {
    private final Shape shape;
    private final int radius;
    private final String nationName;
    public UnclaimTerritoryNationSubCommand(Player sender, Shape shape, int radius, String nationName){
        super(sender);
        this.nationName = nationName;
        this.shape = shape;
        this.radius = radius;
    }
    @Override
    public void execute() {
        final Nation nation;
        Nation owner = null;
        final NationManager manager = NationManager.getInstance();
        NationClaimTerritoryEvent event = null;
        Set<Territory> unclaimedTerritory = new HashSet<>();
        if (nationName.length() > 0) {
            if (!sender.hasPermission("nationcraft.nation.claim.other")) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim for your own nation.");
                return;
            }
            nation = NationManager.getInstance().getNationByName(nationName);
        } else {
            nation = NationManager.getInstance().getNationByPlayer(sender.getUniqueId());
        }
        if (nation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a nation!");
            return;
        }

        if (shape == null || shape == Shape.SINGLE) {
            Chunk c = sender.getLocation().getChunk();
            unclaimedTerritory.add(Territory.fromChunk(c));
        } else if (shape == Shape.CIRCLE){
            nation.getTerritoryManager().unclaimCircularTerritory(sender, radius);
        } else if (shape == Shape.SQUARE){
            nation.getTerritoryManager().unclaimSquareTerritory(sender, radius);
        } else if (shape == Shape.LINE){
            nation.getTerritoryManager().unclaimLineTerritory(sender, radius);
        } else if (shape == Shape.ALL){
            nation.getTerritoryManager().unclaimAll(sender);
        }
        nation.saveToFile();
    }
}
