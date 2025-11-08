package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementUnclaimCommand extends Command {

    public SettlementUnclaimCommand() {
        super("unclaim");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        NCPlayer player = (NCPlayer) sender;
        final Shape shape = args.length > 0 ? Shape.getShape(args[0]) : Shape.SINGLE;
        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (Exception e) {
            radius = 0;
        }
        String settlementName;
        try {
            settlementName = radius > 0 ? args[1] : args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            settlementName = "";
        }
        Settlement settlement;
        if (settlementName.length() > 0){
            if (!sender.hasPermission("nationcraft.settlement.unclaim.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only unclaim for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(settlementName);
        } else {
            settlement = SettlementManager.getInstance().getSettlementByPlayer(player);
        }
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("No settlement called %s exists", settlementName));
            } else {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            }
            return;
        }

        settlement.unclaimTerritory(player, shape, radius);
    }
}
