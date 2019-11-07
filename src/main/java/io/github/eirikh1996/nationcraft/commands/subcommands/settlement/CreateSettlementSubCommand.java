package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import org.bukkit.entity.Player;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class CreateSettlementSubCommand extends SettlementSubCommand {
    private final String settlementName;
    public CreateSettlementSubCommand(Player sender, String settlementName) {
        super(sender);
        this.settlementName = settlementName;
    }

    @Override
    public void execute() {
        Nation testNation = NationManager.getInstance().getNationAt(sender.getLocation());
        Nation pNation = NationManager.getInstance().getNationByPlayer(sender.getUniqueId());
        Settlement existing = SettlementManager.getInstance().getSettlementByName(settlementName);
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
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You successfully created a new settlement named " + settlementName + ".");
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "The chunk you are standing in is now your settlement's town center. This can be relocated using /settlement towncenter set");
        Settlement newSettlement = new Settlement(settlementName, sender);
        if (pNation.getCapital() == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + settlementName + "has been assigned as capital of " + pNation.getName() + ". You can change this by using /settlement setcapital");
            pNation.setCapital(newSettlement);

            pNation.saveToFile();
        }
        pNation.getSettlements().add(newSettlement);
        newSettlement.saveToFile();
    }
}
