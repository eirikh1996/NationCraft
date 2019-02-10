package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.claiming.ClaimUtils;
import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.events.nation.NationClaimTerritoryEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        Set<Chunk> unclaimedTerritory = new HashSet<>();
        if (nationName.length() > 0) {
            if (!sender.hasPermission("nationcraft.nation.claim.other")) {
                sender.sendMessage(Messages.ERROR + "You can only claim for your own nation.");
                return;
            }
            nation = NationManager.getInstance().getNationByName(nationName);
        } else {
            nation = NationManager.getInstance().getNationByPlayer(sender);
        }
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }

        if (shape == null || shape == Shape.SINGLE) {
            Chunk c = sender.getLocation().getChunk();
            unclaimedTerritory.add(c);
        } else if (shape == Shape.CIRCLE){
            unclaimedTerritory = ClaimUtils.claimCircularTerritory(sender,radius);
        } else if (shape == Shape.SQUARE){
            unclaimedTerritory = ClaimUtils.claimSquareTerritory(sender,radius);
        } else if (shape == Shape.LINE){
            unclaimedTerritory = ClaimUtils.claimLineTerritory(sender,radius);
        } else if (shape == Shape.ALL){

        }
        Set<Chunk> toRemove = new HashSet<>();
        Iterator<Chunk> cIter = unclaimedTerritory.iterator();
        while (cIter.hasNext()){
            Chunk c = cIter.next();
            if (nation.getTerritory().contains(c)){
                continue;
            }
            cIter.remove();
        }
        unclaimedTerritory.removeAll(toRemove);
        if (unclaimedTerritory.isEmpty()){
            sender.sendMessage(Messages.ERROR+"No territory to unclaim!");
            return;
        }
        if (nation.getTerritory().removeAll(unclaimedTerritory)){
            sender.sendMessage(String.format("Unclaimed %d pieces of territory", unclaimedTerritory.size()));
        }
    }
}
