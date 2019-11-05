package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import org.bukkit.entity.Player;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class ClaimSettlementSubCommand extends SettlementSubCommand {
    private final Shape shape;
    private final int radius;
    private final String settlementName;
    public ClaimSettlementSubCommand(Player sender, Shape shape, int radius, String settlementName) {
        super(sender);
        this.shape = shape;
        this.radius = radius;
        this.settlementName = settlementName;
    }

    @Override
    public void execute() {
        Settlement settlement;
        if (settlementName.length() > 0){
            if (!sender.hasPermission("nationcraft.settlement.claim.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(settlementName);
        } else {
            settlement = SettlementManager.getInstance().getSettlementByPlayer(sender.getUniqueId());
        }
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("No settlement called %s exists", settlementName));
            } else {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            }
            return;
        }
        Nation pNation = NationManager.getInstance().getNationByPlayer(sender);
        Nation locNation = NationManager.getInstance().getNationAt(sender.getLocation());
        if (locNation != pNation){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim for your settlement within the territory of your own nation");
            return;
        }

        if (shape.equals(Shape.CIRCLE)){
            settlement.getTerritory().claimCircularTerritory(sender, radius);
        } else if (shape.equals(Shape.SQUARE)){
            settlement.getTerritory().claimSquareTerritory(sender, radius);
        } else if (shape.equals(Shape.LINE)){
            settlement.getTerritory().claimLineTerritory(sender, radius);
        } else {
            settlement.getTerritory().claimSignleTerritory(sender);
        }
        settlement.saveToFile();
    }
}
