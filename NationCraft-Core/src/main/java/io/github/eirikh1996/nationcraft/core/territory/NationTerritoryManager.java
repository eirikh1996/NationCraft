package io.github.eirikh1996.nationcraft.core.territory;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import io.github.eirikh1996.nationcraft.api.objects.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.utils.CollectionUtils;
import io.github.eirikh1996.nationcraft.core.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public final class NationTerritoryManager implements TerritoryManager {
    private Collection<Territory> territoryCollection = new HashSet<>();
    private final Nation nation;

    public NationTerritoryManager(Nation nation) {
        this.nation = nation;
    }

    @Override
    public void claimCircularTerritory(NCPlayer player, int radius){
        ArrayList<Territory> claimed = new ArrayList<>();
        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;

        for (int x = cx - radius; x <= cx + radius; x++){
            for (int z = cz - radius; z <= cz + radius; z++){
                NCVector distance = new NCVector(cx - x, 0 , cz - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                claimed.add(new Territory(player.getLocation().getWorld(), x, z));
            }
        }
        Set<Territory> alreadyClaimed = new HashSet<>();
        HashMap<Nation, Set<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, Set<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : claimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (nation == null){
                continue;
            }
            else if (nation.equals(this.nation)){
                alreadyClaimed.add(territory);
            }
            else if (nation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    Set<Territory> canHold = new HashSet<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    Set<Territory> lost = new HashSet<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        int size = size() + CollectionUtils.filter(claimed, territoryCollection).size();
        if (size > nation.getPower() && !player.isAdminMode()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot claim more land. You need more power");
            return;
        }
        ArrayList<Territory> filter = new ArrayList<>();
        if (!alreadyClaimed.isEmpty()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Your nation already owns this land");
            filter.addAll(alreadyClaimed);
        }
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);
    }

    public void unclaimCircularTerritory(NCPlayer player, int radius){
        ArrayList<Territory> unclaimed = new ArrayList<>();
        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;

        for (int x = cx - radius; x <= cx + radius; x++){
            for (int z = cz - radius; z <= cz + radius; z++){
                NCVector distance = new NCVector(cx - x, 0 , cz - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                unclaimed.add(new Territory(player.getLocation().getWorld(), x, z));
            }
        }
        HashMap<Nation, HashSet<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, HashSet<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : unclaimed){
            Nation testNation = NationManager.getInstance().getNationAt(territory);
            if (!nation.equals(testNation) && testNation == null){
                continue;
            }
            else if (!nation.equals(testNation) && testNation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    HashSet<Territory> canHold = new HashSet<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    HashSet<Territory> lost = new HashSet<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Unclaimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> toWilderness = CollectionUtils.filter(unclaimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);
    }
    public void claimSquareTerritory(NCPlayer player, int radius){
        ArrayList<Territory> claimed = new ArrayList<>();
        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;

        for (int x = cx - radius; x <= cx + radius; x++){
            for (int z = cz - radius; z <= cz + radius; z++){
                claimed.add(new Territory(player.getLocation().getWorld(), x, z));
            }
        }
        HashMap<Nation, Set<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, Set<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : claimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (nation == null){
                continue;
            }
            else if (nation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    Set<Territory> canHold = new HashSet<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    Set<Territory> lost = new HashSet<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        int size = size() + CollectionUtils.filter(claimed, territoryCollection).size();
        if (size > nation.getPower() && !nation.isSafezone() && !nation.isWarzone()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot claim more land. You need more power");
            return;
        }
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                Set<Territory> overclaimedTerritory = overclaimedTerritories.get(overclaimed);
                filter.addAll(overclaimedTerritory);
                overclaimed.getTerritoryManager().removeAll(overclaimedTerritory);
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));

                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);
    }

    public void unclaimSquareTerritory(NCPlayer player, int radius){
        ArrayList<Territory> unclaimed = new ArrayList<>();
        int cx = player.getLocation().getBlockX() >> 4;
        int cz = player.getLocation().getBlockZ() >> 4;

        for (int x = cx - radius; x <= cx + radius; x++){
            for (int z = cz - radius; z <= cz + radius; z++){
                unclaimed.add(new Territory(player.getLocation().getWorld(), x, z));
            }
        }
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> safeWarzoneTerritories = new HashMap<>();
        for (Territory territory : unclaimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (!this.nation.equals(nation) && nation == null){
                continue;
            }
            else if (!this.nation.equals(nation) && nation.isStrongEnough()){
                if (player.hasPermission("nationcraft.nation.claim.bypassstrength") || player.hasPermission("nationcraft.admin")){
                    continue;
                }
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    ArrayList<Territory> canHold = new ArrayList<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else if (!this.nation.equals(nation)) {
                if (nation.isSafezone()|| nation.isWarzone()){
                    if (safeWarzoneTerritories.containsKey(nation)){
                        safeWarzoneTerritories.get(nation).add(territory);
                    } else {
                        safeWarzoneTerritories.put(nation, new ArrayList<>());
                        safeWarzoneTerritories.get(nation).add(territory);
                    }
                } else {
                    if (overclaimedTerritories.containsKey(nation)) {
                        overclaimedTerritories.get(nation).add(territory);
                    } else {
                        ArrayList<Territory> lost = new ArrayList<>();
                        lost.add(territory);
                        overclaimedTerritories.put(nation, lost);
                    }
                }
            }
        }
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> toWilderness = CollectionUtils.filter(unclaimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);
    }
    public void claimLineTerritory(NCPlayer player, int distance){
        Set<Territory> claimed = new HashSet<>();
        int count = 0;
        while (count <= distance) {
            switch (Direction.fromYaw(player.getLocation().getYaw())) {
                case WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4)));
                    break;
                case EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4)));
                    break;
                case NORTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4), (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case SOUTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4), (player.getLocation().getBlockZ() >> 4) + count));
                    break;
                case SOUTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4) + count));
                    break;
                case NORTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case NORTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case SOUTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4) + count));
                    break;
            }
            count++;
        }
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : claimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (nation == null){
                continue;
            }
            else if (nation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    ArrayList<Territory> canHold = new ArrayList<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    ArrayList<Territory> lost = new ArrayList<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        int size = size() + CollectionUtils.filter(claimed, territoryCollection).size();
        if (size > nation.getPower() && !nation.isSafezone() && !nation.isWarzone()){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot claim more land. You need more power");
            return;
        }
        Set<Territory> filter = new HashSet<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        Collection<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);

    }

    public void unclaimLineTerritory(NCPlayer player, int distance){
        Set<Territory> unclaimed = new HashSet<>();
        int count = 0;
        while (count <= distance) {
            switch (Direction.fromYaw(player.getLocation().getYaw())) {
                case WEST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4)));
                    break;
                case EAST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4)));
                    break;
                case NORTH:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4), (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case SOUTH:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4), (player.getLocation().getBlockZ() >> 4) + count));
                    break;
                case SOUTH_EAST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4) + count));
                    break;
                case NORTH_EAST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) + count, (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case NORTH_WEST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4) - count));
                    break;
                case SOUTH_WEST:
                    unclaimed.add(new Territory(player.getLocation().getWorld(), (player.getLocation().getBlockX() >> 4) - count, (player.getLocation().getBlockZ() >> 4) + count));
                    break;
            }
            count++;
        }
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : unclaimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (nation == null){
                continue;
            }
            else if (nation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    ArrayList<Territory> canHold = new ArrayList<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    ArrayList<Territory> lost = new ArrayList<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        Set<Territory> filter = new HashSet<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        Collection<Territory> toWilderness = CollectionUtils.filter(unclaimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);

    }

    @Override
    public void claimSignleTerritory(NCPlayer player) {
        Territory territory = new Territory(player.getLocation().getWorld(), player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4);
        Nation nation = NationManager.getInstance().getNationAt(territory);
        if (nation != null){
            if (nation.isStrongEnough() && player.isAdminMode()){
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s owns this land and is strong enough to hold it", NationManager.getInstance().getColor(player, nation) + nation.getName() + TextColor.RESET));
                return;
            }
            if (nation == this.nation) {
                player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Your nation already owns this land");
                return;
            }
            nation.getTerritoryManager().remove(territory);
        }
        if (territoryCollection.size() + 1 > this.nation.getPower() && !nation.isSafezone() && !nation.isWarzone()) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot claim more land. You need more power");
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Claimed one chunk of territory from " + (nation == null ? TextColor.DARK_GREEN + "Wilderness" : nation.getName(this.nation)));
        territoryCollection.add(territory);
    }

    @Override
    public void unclaimSignleTerritory(NCPlayer player) {

    }

    public void unclaimAll(NCPlayer player){
        player.sendMessage(String.format("Unclaimed all of %s's territory", nation.getName()));
        territoryCollection.clear();
    }

    @Override
    public boolean add(Territory territory) {
        return territoryCollection.add(territory);
    }

    public boolean addAll(Collection<? extends Territory> territories){
        return territoryCollection.addAll(territories);
    }

    @Override
    public boolean remove(Territory territory) {
        return territoryCollection.remove(territory);
    }

    @Override
    public boolean removeAll(Collection<? extends Territory> territories) {
        return territoryCollection.removeAll(territories);
    }

    public int size(){
        return territoryCollection.size();
    }

    public boolean contains(Territory territory){
        return territoryCollection.contains(territory);
    }
    public boolean isEmpty(){
        return territoryCollection.isEmpty();
    }
    @NotNull
    @Override
    public Iterator<Territory> iterator() {
        return Collections.unmodifiableCollection(territoryCollection).iterator();
    }
}
