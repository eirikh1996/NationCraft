package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        if (Settings.Debug){
            Bukkit.broadcastMessage(String.format("Claim territory %s, %d", shape.name(), radius));
        }
        final Nation nation;
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

        if (shape == Shape.SINGLE) {

        } else if (shape == Shape.CIRCLE) {
            nation.getTerritoryManager().claimCircularTerritory(sender, radius);
        } else if (shape.equals(Shape.SQUARE)) {
            nation.getTerritoryManager().claimSquareTerritory(sender, radius);
        } else if (shape.equals(Shape.LINE)) {
            nation.getTerritoryManager().claimLineTerritory(sender, radius);
        }
        nation.saveToFile();
    }
}
