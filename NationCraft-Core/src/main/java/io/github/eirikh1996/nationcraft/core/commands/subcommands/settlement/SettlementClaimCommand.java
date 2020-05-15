package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementClaimCommand extends Command {
    public SettlementClaimCommand() {
        super("claim");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        Settlement settlement;
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        final Shape shape = args.length > 0 ? Shape.getShape(args[0]) : Shape.SINGLE;
        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (Exception e) {
            radius = 0;
        }
        String settlementName;
        try {
            settlementName = shape == Shape.SINGLE ? args[1] : args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            settlementName = "";
        }
        if (settlementName.length() > 0){
            if (!sender.hasPermission("nationcraft.settlement.claim.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(settlementName);
        } else {
            settlement = player.getSettlement();
        }
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("No settlement called %s exists", settlementName));
            } else {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            }
            return;
        }
        Nation pNation = player.getNation();
        Nation locNation = player.getLocation().getNation();
        if (locNation != pNation){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only claim for your settlement within the territory of your own nation");
            return;
        }

        if (shape.equals(Shape.CIRCLE)){
            settlement.getTerritory().claimCircularTerritory(player, radius);
        } else if (shape.equals(Shape.SQUARE)){
            settlement.getTerritory().claimSquareTerritory(player, radius);
        } else if (shape.equals(Shape.LINE)){
            settlement.getTerritory().claimLineTerritory(player, radius);
        } else {
            settlement.getTerritory().claimSignleTerritory(player);
        }
        settlement.saveToFile();
    }
}
