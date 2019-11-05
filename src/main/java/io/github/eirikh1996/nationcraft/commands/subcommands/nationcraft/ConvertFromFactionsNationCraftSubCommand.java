package io.github.eirikh1996.nationcraft.commands.subcommands.nationcraft;

import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.messages.Messages.*;
import org.json.simple.JSONObject;

import java.io.File;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class ConvertFromFactionsNationCraftSubCommand extends NationCraftSubCommand {
    public ConvertFromFactionsNationCraftSubCommand(Player sender) {
        super(sender);
    }

    @Override
    public void execute() {
        File mstore = new File("mstore");
        if (!mstore.exists()){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Factions has never been run on the server");
            return;
        }
        JSONObject jo = new JSONObject();
        final File factionsDir = new File(mstore, "factions_faction");
        for (File factionData : factionsDir.listFiles()){
            if (factionData.getName().contains("none")){
                continue;
            }

        }
    }
}
