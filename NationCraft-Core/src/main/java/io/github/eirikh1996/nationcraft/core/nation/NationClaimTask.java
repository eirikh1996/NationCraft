package io.github.eirikh1996.nationcraft.core.nation;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.events.nation.NationClaimEvent;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import io.github.eirikh1996.nationcraft.api.territory.TerritoryManager;
import io.github.eirikh1996.nationcraft.api.utils.CollectionUtils;
import io.github.eirikh1996.nationcraft.api.utils.Direction;
import io.github.eirikh1996.nationcraft.core.Core;
import io.github.eirikh1996.nationcraft.core.claiming.ClaimTask;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationClaimTask extends ClaimTask {
    private final Nation nation;
    public NationClaimTask(Nation nation, @NotNull NCPlayer player, @NotNull Shape shape, int radius) {
        super(player, shape, radius);
        this.nation = nation;
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        Collection<Territory> claims;
        Territory origin = player.getLocation().getTerritory();
        if (shape == Shape.LINE) {
            claims = origin.line(Direction.fromYaw(player.getLocation().getYaw()), radius);
        } else if (shape == Shape.ALL) {
            throw new IllegalArgumentException("Invalid shape " + shape);
        } else if (shape == Shape.SINGLE) {
            claims = new HashSet<>();
            claims.add(origin);
        } else {
            claims = player.getLocation().getTerritory().adjacent(shape, radius);
        }
        //Remove territory already claimed by the nation
        //claims.removeIf(claim -> nation.getTerritory().contains(claim));
        //Then check if claimed territory is claimed by other nations
        Set<Territory> alreadyClaimed = new HashSet<>();
        HashMap<Nation, Set<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, Set<Territory>> overclaimedTerritories = new HashMap<>();
        Set<Territory> settlementLand = new HashSet<>();
        for (Territory territory : claims){
            Nation foundNation = territory.getNation();
            //Ignore territories not belonging to a nation
            if (foundNation == null){
                continue;
            }
            //If there is a settlement on the land, do not overclaim them, they can be taken through sieges
            if (territory.getSettlement() != null) {
                settlementLand.add(territory);
            }
            if (foundNation.equals(this)){
                alreadyClaimed.add(territory);
            }
            if (foundNation.isStrongEnough() && !nation.isWarzone() && !nation.isSafezone()){
                if (strongEnoughNations.containsKey(foundNation)){
                    strongEnoughNations.get(foundNation).add(territory);
                } else {
                    Set<Territory> canHold = new HashSet<>();
                    canHold.add(territory);
                    strongEnoughNations.put(foundNation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(foundNation)){
                    overclaimedTerritories.get(foundNation).add(territory);
                } else {
                    Set<Territory> lost = new HashSet<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        Collection<Territory> territoryColl = new HashSet<>(nation.getTerritory());
        int size = territoryColl.size() + CollectionUtils.filter(claims, territoryColl).size();
        if (size > nation.getPower() && !player.isAdminMode()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You cannot claim more land. You need more power", ERROR.color())));
            return;
        }
        Set<Territory> filter = new HashSet<>();
        if (!alreadyClaimed.isEmpty()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Your nation already owns this land")));
            filter.addAll(alreadyClaimed);
        }
        if (!settlementLand.isEmpty()) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Settlement land cannot be overclaimed. Use /settlement siege to conquer settlements")));
            filter.addAll(settlementLand);
        }
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                Set<Territory> lost = overclaimedTerritories.get(overclaimed);
                TerritoryManager.getInstance().removeNationTerritory(overclaimed, lost);
                filter.addAll(lost);
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(String.format("Claimed %d chunks of land from nation ", overclaimedTerritories.get(overclaimed).size() )).append(overclaimed.getName(nation)) ));
                TerritoryManager.getInstance().addNationTerritory(nation, overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(strongEnoughNations.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(strongEnough.getName(nation)).append(Component.text(" owns this land and is strong enough to hold it")));
            }
        }
        Collection<Territory> fromWilderness = CollectionUtils.filter(claims, filter);
        Collection<Territory> eventTerritory = new HashSet<>(fromWilderness);
        overclaimedTerritories.forEach((n, ts) -> { eventTerritory.addAll(ts); });
        if (fromWilderness.isEmpty()){
            return;
        }
        NationClaimEvent event = new NationClaimEvent(nation, player, eventTerritory);
        NationCraftAPI.getInstance().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Claimed " + fromWilderness.size() + " chunks of territory from ")).append(WILDERNESS));
        TerritoryManager.getInstance().addNationTerritory(nation, claims.stream().filter(claim -> !nation.getTerritory().contains(claim)).collect(Collectors.toSet()));
        final long time = System.currentTimeMillis() - start;
        Core.getMain().logInfo("Territory claiming for " + nation.getName() + " took " + time + " milliseconds");
    }

    public Nation getNation() {
        return nation;
    }
}
