package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import org.bukkit.entity.Player;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class UnclaimSettlementSubCommand extends SettlementSubCommand {
    private final Shape shape;
    private final int radius;
    private final String settlementName;
    public UnclaimSettlementSubCommand(Player sender, Shape shape, int radius, String settlementName) {
        super(sender);
        this.shape = shape;
        this.radius = radius;
        this.settlementName = settlementName;
    }

    @Override
    public void execute() {
        Settlement settlement;
        if (settlementName.length() > 0){
            if (!sender.hasPermission("nationcraft.settlement.unclaim.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only unclaim for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(settlementName);
        } else {
            settlement = SettlementManager.getInstance().getSettlementByPlayer(sender);
        }
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("No settlement called %s exists", settlementName));
            } else {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            }
            return;
        }

        if (shape.equals(Shape.CIRCLE)){
            settlement.getTerritory().unclaimCircularTerritory(sender, radius);
        } else if (shape.equals(Shape.SQUARE)){
            settlement.getTerritory().unclaimSquareTerritory(sender, radius);
        } else if (shape.equals(Shape.LINE)){
            settlement.getTerritory().unclaimLineTerritory(sender, radius);
        } else if (shape.equals(Shape.ALL)){
            settlement.getTerritory().unclaimAll(sender);
        } else {
            settlement.getTerritory().unclaimSignleTerritory(sender);
        }
    }
}
