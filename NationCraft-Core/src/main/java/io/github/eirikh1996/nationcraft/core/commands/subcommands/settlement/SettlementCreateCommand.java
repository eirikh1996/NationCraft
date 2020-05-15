package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementCreateCommand extends Command {
    
    public SettlementCreateCommand() {
        super("create");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a name");
            return;
        }
        NCPlayer player = (NCPlayer) sender;
        Nation testNation = player.getLocation().getNation();
        Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        Settlement existing = SettlementManager.getInstance().getSettlementByName(args[0]);
        if (pNation == null){//Reject attempts to create new settlement if settlement doesn't exist
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must be part of a nation to create a settlement");
            return;
        } else if (existing != null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("A settlement named %s already exists", existing.getName()));
            return;
        }else if (testNation == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot create a settlement in the wilderness");
            return;
        } else if (!pNation.equals(testNation)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot create settlements on land belonging to other nations");
            return;
        }
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You successfully created a new settlement named " + args[0] + ".");
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "The chunk you are standing in is now your settlement's town center. This can be relocated using /settlement towncenter set");
        Settlement newSettlement = new Settlement(args[0], player);
        if (pNation.getCapital() == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + args[0] + "has been assigned as capital of " + pNation.getName() + ". You can change this by using /settlement setcapital");
            pNation.setCapital(newSettlement);

            pNation.saveToFile();
        }
        pNation.getSettlements().add(newSettlement);
        newSettlement.saveToFile();
    }
}
