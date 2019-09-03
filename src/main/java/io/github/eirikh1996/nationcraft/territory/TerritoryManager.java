package io.github.eirikh1996.nationcraft.territory;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.utils.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TerritoryManager implements Iterable<Territory> {
    private Collection<Territory> territoryCollection = new CopyOnWriteArrayList<>();
    private final Nation nation;

    public TerritoryManager(Nation nation) {
        this.nation = nation;
    }

    public void claimCircularTerritory(Player player, int radius){
        Bukkit.broadcastMessage("test");
        ArrayList<Territory> claimed = new ArrayList<>();
        Chunk pChunk = player.getLocation().getChunk();

        for (int x = pChunk.getX() - radius; x <= pChunk.getX() + radius; x++){
            for (int z = pChunk.getZ() - radius; z <= pChunk.getZ() + radius; z++){
                Vector distance = new Vector(pChunk.getX() - x, 0 , pChunk.getZ() - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                claimed.add(new Territory(player.getWorld(), x, z));
            }
        }
        ArrayList<Territory> alreadyClaimed = new ArrayList<>();
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
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
        ArrayList<Territory> filter = new ArrayList<>();
        if (!alreadyClaimed.isEmpty()){
            player.sendMessage("Your nation already owns this land");
            filter.addAll(alreadyClaimed);
        }
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);
    }

    public void unclaimCircularTerritory(Player player, int radius){
        ArrayList<Territory> unclaimed = new ArrayList<>();
        Chunk pChunk = player.getLocation().getChunk();
        Vector distance = new Vector(pChunk.getX(), 0 , pChunk.getZ());
        for (int x = pChunk.getX() - radius; x <= pChunk.getX() + radius; x++){
            for (int z = pChunk.getZ() - radius; z <= pChunk.getZ() + radius; z++){
                if (distance.length() > radius){
                    continue;
                }
                unclaimed.add(new Territory(player.getWorld(), x, z));
            }
        }
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : unclaimed){
            Nation testNation = NationManager.getInstance().getNationAt(territory);
            if (!nation.equals(testNation) && testNation == null){
                continue;
            }
            else if (!nation.equals(testNation) && testNation.isStrongEnough()){
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
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(String.format("Unclaimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> toWilderness = CollectionUtils.filter(unclaimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);
    }
    public void claimSquareTerritory(Player player, int radius){
        ArrayList<Territory> claimed = new ArrayList<>();
        Chunk pChunk = player.getLocation().getChunk();
        Vector distance = new Vector(pChunk.getX(), 0 , pChunk.getZ());
        for (int x = pChunk.getX() - radius; x <= pChunk.getX() + radius; x++){
            for (int z = pChunk.getZ() - radius; z <= pChunk.getZ() + radius; z++){
                claimed.add(new Territory(player.getWorld(), x, z));
            }
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
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);
    }

    public void unclaimSquareTerritory(Player player, int radius){
        ArrayList<Territory> unclaimed = new ArrayList<>();
        Chunk pChunk = player.getLocation().getChunk();
        for (int x = pChunk.getX() - radius; x <= pChunk.getX() + radius; x++){
            for (int z = pChunk.getZ() - radius; z <= pChunk.getZ() + radius; z++){
                unclaimed.add(new Territory(player.getWorld(), x, z));
            }
        }
        HashMap<Nation, ArrayList<Territory>> strongEnoughNations = new HashMap<>();
        HashMap<Nation, ArrayList<Territory>> overclaimedTerritories = new HashMap<>();
        for (Territory territory : unclaimed){
            Nation nation = NationManager.getInstance().getNationAt(territory);
            if (!this.nation.equals(nation) && nation == null){
                continue;
            }
            else if (!this.nation.equals(nation) && nation.isStrongEnough()){
                if (strongEnoughNations.containsKey(nation)){
                    strongEnoughNations.get(nation).add(territory);
                } else {
                    ArrayList<Territory> canHold = new ArrayList<>();
                    canHold.add(territory);
                    strongEnoughNations.put(nation, canHold);
                }
            } else if (!this.nation.equals(nation)) {
                if (overclaimedTerritories.containsKey(nation)){
                    overclaimedTerritories.get(nation).add(territory);
                } else {
                    ArrayList<Territory> lost = new ArrayList<>();
                    lost.add(territory);
                    overclaimedTerritories.put(nation, lost);
                }
            }
        }
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                player.sendMessage(String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> toWilderness = CollectionUtils.filter(unclaimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);
    }
    public void claimLineTerritory(Player player, int distance){
        ArrayList<Territory> claimed = new ArrayList<>();
        int count = 0;
        switch (player.getFacing()){
            case UP:
            case DOWN:
                player.sendMessage(String.format(Messages.ERROR + "Invalid direction: %s", player.getFacing().name().toLowerCase()));
                return;
            case WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ()));
                    count++;
                }
                break;
            case EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ()));
                    count++;
                }
                break;
            case NORTH:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case SOUTH:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
            case SOUTH_EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
            case NORTH_EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case NORTH_WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case SOUTH_WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
        }
        Bukkit.broadcastMessage(player.getFacing().name());
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
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                filter.addAll(overclaimedTerritories.get(overclaimed));
                player.sendMessage(String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.addAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> fromWilderness = CollectionUtils.filter(claimed, filter);
        if (fromWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Claimed %d chunks of land from Wilderness", fromWilderness.size()));
        territoryCollection.addAll(fromWilderness);

    }

    public void unclaimLineTerritory(Player player, int distance){
        ArrayList<Territory> claimed = new ArrayList<>();
        int count = 0;
        switch (player.getFacing()){
            case UP:
            case DOWN:
                player.sendMessage(String.format(Messages.ERROR + "Invalid direction: %s", player.getFacing().name().toLowerCase()));
                return;
            case WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ()));
                    count++;
                }
                break;
            case EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ()));
                    count++;
                }
                break;
            case NORTH:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case SOUTH:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
            case SOUTH_EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
            case NORTH_EAST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() + count, player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case NORTH_WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ() - count));
                    count++;
                }
                break;
            case SOUTH_WEST:
                while (count <= distance){
                    claimed.add(new Territory(player.getWorld(), player.getLocation().getChunk().getX() - count, player.getLocation().getChunk().getZ() + count));
                    count++;
                }
                break;
        }
        Bukkit.broadcastMessage(player.getFacing().name());
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
        ArrayList<Territory> filter = new ArrayList<>();
        if (!overclaimedTerritories.isEmpty()){
            for (Nation overclaimed : overclaimedTerritories.keySet()){
                player.sendMessage(String.format("Claimed %d chunks of land from nation %s", overclaimedTerritories.get(overclaimed).size(), overclaimed.getName()));
                territoryCollection.removeAll(overclaimedTerritories.get(overclaimed));
            }
        }
        if (!strongEnoughNations.isEmpty()){
            for (Nation strongEnough : strongEnoughNations.keySet()){
                filter.addAll(overclaimedTerritories.get(strongEnough));
                player.sendMessage(String.format("%s owns this land and is strong enough to hold it", strongEnough.getName()));
            }
        }
        ArrayList<Territory> toWilderness = CollectionUtils.filter(claimed, filter);
        if (toWilderness.isEmpty()){
            return;
        }
        player.sendMessage(String.format("Unclaimed %d chunks of land from %s", toWilderness.size(), nation.getName()));
        territoryCollection.removeAll(toWilderness);

    }

    public void unclaimAll(Player player){
        player.sendMessage(String.format("Unclaimed all of %s's territory", nation.getName()));
        territoryCollection.clear();
    }
    public boolean addAll(Collection<? extends Territory> territories){
        return territoryCollection.addAll(territories);
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
