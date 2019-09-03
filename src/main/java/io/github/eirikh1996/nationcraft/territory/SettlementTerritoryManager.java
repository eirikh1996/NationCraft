package io.github.eirikh1996.nationcraft.territory;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.utils.CollectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public final class SettlementTerritoryManager implements TerritoryManager {
    private Collection<Territory> territoryCollection = new CopyOnWriteArrayList<>();
    private final Settlement settlement;

    public SettlementTerritoryManager(Settlement settlement) {
        this.settlement = settlement;
    }

    @Override
    public void claimCircularTerritory(@NotNull Player player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunk().getX();
        int origZ = player.getLocation().getChunk().getZ();
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Vector distance = new Vector(origX - x , 0 , origZ - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                Territory territory = new Territory(player.getWorld(), x, z);
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
            if (territoryCollection.contains(territory) || settlement.getTownCenter().equals(territory)){
                filter.add(territory);
            }
        }
        territoryCollection.addAll(CollectionUtils.filter(claimed, filter));
    }

    @Override
    public void unclaimCircularTerritory(Player player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunk().getX();
        int origZ = player.getLocation().getChunk().getZ();
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Vector distance = new Vector(origX - x , 0 , origZ - z);
                if ((int) distance.length() > radius){
                    continue;
                }
                Territory territory = new Territory(player.getWorld(), x, z);
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
    public void claimSquareTerritory(Player player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunk().getX();
        int origZ = player.getLocation().getChunk().getZ();
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Territory territory = new Territory(player.getWorld(), x, z);
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
            if (territoryCollection.contains(territory) || settlement.getTownCenter().equals(territory)){
                filter.add(territory);
            }
        }
        territoryCollection.addAll(CollectionUtils.filter(claimed, filter));
    }

    @Override
    public void unclaimSquareTerritory(Player player, int radius) {
        @NotNull final ArrayList<Territory> claimed = new ArrayList<>();
        @Nullable final Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        int origX = player.getLocation().getChunk().getX();
        int origZ = player.getLocation().getChunk().getZ();
        for (int x = origX - radius; x <= origX + radius; x++){
            for (int z = origZ - radius; z <= origZ + radius; z++){
                Territory territory = new Territory(player.getWorld(), x, z);
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
    public void claimLineTerritory(Player player, int distance) {

    }

    @Override
    public void unclaimLineTerritory(Player player, int distance) {

    }

    @Override
    public void unclaimAll(Player player) {

    }

    @Override
    public boolean addAll(Collection<? extends Territory> territories) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(Territory territory) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NotNull
    @Override
    public Iterator<Territory> iterator() {
        return null;
    }
}
