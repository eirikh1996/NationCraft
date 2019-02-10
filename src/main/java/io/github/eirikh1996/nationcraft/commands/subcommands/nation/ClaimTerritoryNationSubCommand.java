package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.claiming.ClaimUtils;
import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.events.nation.NationClaimTerritoryEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ClaimTerritoryNationSubCommand extends NationSubCommand {
    private final Shape shape;
    private final int radius;
    private final String nationName;
    public ClaimTerritoryNationSubCommand(Player sender, Shape shape, int radius, String nationName){
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
        Set<Chunk> claimedTerritory = new HashSet<>();
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

        if (shape == null) {
            Chunk claim = sender.getLocation().getChunk();
            Nation foundNation = manager.getNationAt(claim);


            if (foundNation != null && foundNation != nation) {
                if (foundNation.isStrongEnough()) {
                    sender.sendMessage(foundNation.getName() + " owns this land and is strong enough to hold it.");
                    return;
                } else if (foundNation.getName().equalsIgnoreCase("safezone") || foundNation.getName().equalsIgnoreCase("warzone")) {
                    sender.sendMessage("You cannot claim from " + foundNation.getName() + "'s territory!");
                    return;
                } else {
                    event = new NationClaimTerritoryEvent(nation, claimedTerritory);
                    sender.sendMessage("You claimed 1 piece of land from " + foundNation.getName());
                    for (UUID id : foundNation.getPlayers().keySet()) {
                        Player p = Bukkit.getPlayer(id);
                        if (p == null) {
                            continue;
                        }
                        p.sendMessage(nation.getName() + " claimed 1 piece of territory from your land!");
                    }
                    claimedTerritory.add(claim);
                }
            } else {

                event = new NationClaimTerritoryEvent(nation, claimedTerritory);
                sender.sendMessage("You claimed 1 piece of land from " + ChatColor.DARK_GREEN + "Wilderness");
                claimedTerritory.add(claim);
            }


        } else if (shape == Shape.CIRCLE) {
            claimedTerritory = ClaimUtils.claimCircularTerritory(sender, radius);
            boolean alreadyOwning = false;
            boolean strongEnough = false;
            Set<Chunk> toRemove = new HashSet<>();
            for (Chunk c : claimedTerritory) {
                owner = NationManager.getInstance().getNationAt(c);
                if (owner == nation) {
                    alreadyOwning = true;
                    toRemove.add(c);
                } else if (owner != null && owner != nation) {
                    if (owner.isStrongEnough()) {
                        strongEnough = true;
                        toRemove.add(c);
                    }
                }
            }
            claimedTerritory.removeAll(toRemove);
            if (alreadyOwning) {
                sender.sendMessage("Your nation already owns this land");
            }
            if (strongEnough) {
                sender.sendMessage("");
            }

            event = new NationClaimTerritoryEvent(nation, claimedTerritory);
        } else if (shape.equals(Shape.SQUARE)) {
            claimedTerritory = ClaimUtils.claimSquareTerritory(sender, radius);
            boolean alreadyOwning = false;
            boolean strongEnough = false;
            Set<Chunk> toRemove = new HashSet<>();
            for (Chunk c : claimedTerritory) {
                owner = NationManager.getInstance().getNationAt(c);
                if (owner == nation) {
                    alreadyOwning = true;
                    toRemove.add(c);
                } else if (owner != null && owner != nation) {
                    if (owner.isStrongEnough()) {
                        strongEnough = true;
                        toRemove.add(c);
                    }
                }
            }
            claimedTerritory.removeAll(toRemove);
            if (alreadyOwning) {
                sender.sendMessage("Your nation already owns this land");
            }
            if (strongEnough) {
                sender.sendMessage("");
            }

            event = new NationClaimTerritoryEvent(nation, claimedTerritory);
        } else if (shape.equals(Shape.LINE)) {

        }
        if (nationName != null) {
            if (!sender.hasPermission("nation.claim.others")) {
                sender.sendMessage(Messages.ERROR + "You can only claim territory for your own nation");
                return;
            }
        }
        NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        final Set<Chunk> origTerr = new HashSet<>(nation.getTerritory());
        nation.getTerritory().addAll(claimedTerritory);
        final Set<Chunk> changedTerr = nation.getTerritory();
        NationCraft.getInstance().getLogger().info("Original territory size: " + origTerr.size());
        NationCraft.getInstance().getLogger().info("Claimed territory size: " + claimedTerritory.size());
        NationCraft.getInstance().getLogger().info("Changed territory size: " + changedTerr.size());
        if (origTerr.size() != changedTerr.size()) {
            manager.saveNationToFile(nation);
            final String yourNationName = manager.getColor(sender, nation) + nation.getName() + ChatColor.RESET;
            final String ownerName = owner == null ? Messages.WILDERNESS : manager.getColor(sender, owner) + owner.getName();
            sender.sendMessage(String.format(Messages.CLAIMED_TERRITORY, yourNationName, claimedTerritory.size(), ownerName));

        }
    }
}
