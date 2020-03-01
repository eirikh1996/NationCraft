package io.github.eirikh1996.nationcraft.core.territory;

import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.utils.CollectionUtils;
import io.github.eirikh1996.nationcraft.core.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public final class SettlementTerritoryManager implements TerritoryManager {
    private ArrayList<Territory> territoryCollection = new ArrayList<>();
    private final Settlement settlement;

    public SettlementTerritoryManager(Settlement settlement) {
        this.settlement = settlement;
    }

    /**
     * Claims a circular territory centered on the chunk the player is standing in
     * @param player The player claiming the territory
     * @param radius The radius of the claim
     */
    @Override
    public void claimCircularTerritory(@NotNull NCPlayer player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getBlockX() >> 4;
        int origZ = player.getLocation().getBlockZ() >> 4;
        if (size() <= 1 && !settlement.getTownCenter().equalsTerritory(new Territory(player.getLocation().getWorld(), origX, origZ))) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your can only claim from the town center");
            return;
        }
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                NCVector distance = new NCVector(origX - x , 0 , origZ - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                Territory territory = new Territory(player.getLocation().getWorld(), x, z);
                Nation locN = NationManager.getInstance().getNationAt(territory);
                if (locN == null){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlements cannot be claimed in the wilderness");
                    return;
                }
                else if (!locN.equals(pNation)){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim settlements on lands of your own nation");
                    return;
                }
                claimed.add(territory);
            }
        }
        boolean boundersSettlement = false;
        if (size() > 1) {
            for (Territory territory : claimed) {
                if (!settlement.getTerritory().contains(territory) && !settlement.getTerritory().contains(territory.getRelative(Direction.NORTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.SOUTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.WEST)) && !settlement.getTerritory().contains(territory.getRelative(Direction.EAST))) {
                    continue;
                }
                boundersSettlement = true;
            }
        }
        if (!boundersSettlement){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your claim does not bounder or overlap your settlement");
        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (territoryCollection.contains(territory) || settlement.getTownCenter().equals(territory)){
                filter.add(territory);
            }
        }
        ArrayList<Territory> toAdd = CollectionUtils.filter(claimed, filter);
        //Call event
        territoryCollection.addAll(toAdd);
    }

    @Override
    public void unclaimCircularTerritory(NCPlayer player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunkX();
        int origZ = player.getLocation().getChunkZ();

        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                NCVector distance = new NCVector(origX - x , 0 , origZ - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                Territory territory = new Territory(player.getLocation().getWorld(), x, z);
                Nation locN = NationManager.getInstance().getNationAt(territory);
                if (locN == null){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlements cannot be claimed in the wilderness");
                    return;
                }
                else if (!locN.equals(pNation)){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim settlements on lands of your own nation");
                    return;
                }
                claimed.add(territory);
            }
        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (settlement.getTownCenter().equals(territory)){
                filter.add(territory);
            }
        }
        territoryCollection.removeAll(CollectionUtils.filter(claimed, filter));
    }

    @Override
    public void claimSquareTerritory(NCPlayer player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunkX();
        int origZ = player.getLocation().getChunkZ();
        if (size() <= 1 && !settlement.getTownCenter().equalsTerritory(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ()))){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your can only claim from the town center");
            return;
        } else if (!contains(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ()))){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your can only claim from within yhe settlement territory");
            return;

        }
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Territory territory = new Territory(player.getLocation().getWorld(), x, z);
                Nation locN = NationManager.getInstance().getNationAt(territory);
                if (locN == null){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlements cannot be claimed in the wilderness");
                    return;
                }
                else if (!locN.equals(pNation)){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim settlements on lands of your own nation");
                    return;
                }
                claimed.add(territory);
            }
        }
        boolean boundersSettlement = false;
        for (Territory territory : claimed){
            if (!settlement.getTerritory().contains(territory) && !settlement.getTerritory().contains(territory.getRelative(Direction.NORTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.SOUTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.WEST)) && !settlement.getTerritory().contains(territory.getRelative(Direction.EAST))){
                continue;
            }
            boundersSettlement = true;
        }
        if (!boundersSettlement){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your claim does not bounder or overlap your settlement");
        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (territoryCollection.contains(territory) || settlement.getTownCenter().equalsTerritory(territory)){
                filter.add(territory);
            }
        }
        ArrayList<Territory> toAdd = CollectionUtils.filter(claimed, filter);
        territoryCollection.addAll(toAdd);
    }

    @Override
    public void unclaimSquareTerritory(NCPlayer player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunkX();
        int origZ = player.getLocation().getChunkZ();
        if (size() <= 1 && !settlement.getTownCenter().equalsTerritory(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ()))){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your can only claim from the town center");
            return;
        } else if (!contains(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ()))){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your can only claim from within yhe settlement territory");
            return;

        }
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Territory territory = new Territory(player.getLocation().getWorld(), x, z);
                Nation locN = NationManager.getInstance().getNationAt(territory);
                if (locN == null){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlements cannot be claimed in the wilderness");
                    return;
                }
                else if (!locN.equals(pNation)){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim settlements on lands of your own nation");
                    return;
                }
                claimed.add(territory);
            }
        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (settlement.getTownCenter().equalsTerritory(territory)){
                filter.add(territory);
            }
        }
        territoryCollection.removeAll(CollectionUtils.filter(claimed, filter));
    }

    @Override
    public void claimLineTerritory(NCPlayer player, int distance){
        ArrayList<Territory> claimed = new ArrayList<>();
        int count = 0;
        while (count <= distance) {
            switch (Direction.fromYaw(player.getLocation().getYaw())) {
                case WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ()));
                    break;
                case EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ()));
                    break;
                case NORTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ() - count));
                    break;
                case SOUTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ() + count));
                    break;
                case SOUTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ() + count));
                    break;
                case NORTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ() - count));
                    break;
                case NORTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ() - count));
                    break;
                case SOUTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ() + count));
                    break;
            }
            count++;

        }
        boolean boundersSettlement = false;
        for (Territory territory : claimed){
            if (!settlement.getTerritory().contains(territory) && !settlement.getTerritory().contains(territory.getRelative(Direction.NORTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.SOUTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.WEST)) && !settlement.getTerritory().contains(territory.getRelative(Direction.EAST))){
                continue;
            }
            boundersSettlement = true;
        }
        if (!boundersSettlement){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your claim does not bounder or overlap your settlement");
        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (settlement.getTownCenter().equalsTerritory(territory)){
                filter.add(territory);
            }
        }
        ArrayList<Territory> toAdd = CollectionUtils.filter(claimed, filter);
        //Call event
        territoryCollection.addAll(toAdd);
    }

    @Override
    public void unclaimLineTerritory(NCPlayer player, int distance) {
        ArrayList<Territory> claimed = new ArrayList<>();
        int count = 0;
        while (count <= distance) {
            switch (Direction.fromYaw(player.getLocation().getYaw())) {
                case WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ()));
                    break;
                case EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ()));
                    break;
                case NORTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ() - count));
                    break;
                case SOUTH:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ() + count));
                    break;
                case SOUTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ() + count));
                    break;
                case NORTH_EAST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() + count, player.getLocation().getChunkZ() - count));
                    break;
                case NORTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ() - count));
                    break;
                case SOUTH_WEST:
                    claimed.add(new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX() - count, player.getLocation().getChunkZ() + count));
                    break;
            }
            count++;

        }
        ArrayList<Territory> filter = new ArrayList<>();
        for (Territory territory : claimed){
            if (settlement.getTownCenter().equalsTerritory(territory)){
                filter.add(territory);
            }
        }
        territoryCollection.removeAll(CollectionUtils.filter(claimed, filter));
    }

    @Override
    public void claimSignleTerritory(NCPlayer player) {
        Territory territory = new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ());
        if (!settlement.getTerritory().contains(territory) && !settlement.getTerritory().contains(territory.getRelative(Direction.NORTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.SOUTH)) && !settlement.getTerritory().contains(territory.getRelative(Direction.WEST)) && !settlement.getTerritory().contains(territory.getRelative(Direction.EAST))){
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Your claim does not bounder or overlap your settlement");
            return;
        }
        ArrayList<Territory> toAdd = new ArrayList<>();
        toAdd.add(territory);
        //Call event
        territoryCollection.addAll(toAdd);
    }

    @Override
    public void unclaimSignleTerritory(NCPlayer player) {
        Territory territory = new Territory(player.getLocation().getWorld(), player.getLocation().getChunkX(), player.getLocation().getChunkZ());
        territoryCollection.remove(territory);
    }

    @Override
    public void unclaimAll(NCPlayer player) {
        territoryCollection.clear();
    }

    @Override
    public boolean add(Territory territory) {
        return territoryCollection.add(territory);
    }

    @Override
    public boolean addAll(Collection<? extends Territory> territories) {
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

    @Override
    public int size() {
        return territoryCollection.size();
    }

    @Override
    public boolean contains(Territory territory) {
        return territoryCollection.contains(territory);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NotNull
    @Override
    public Iterator<Territory> iterator() {
        return Collections.unmodifiableCollection(territoryCollection).iterator();
    }
}
