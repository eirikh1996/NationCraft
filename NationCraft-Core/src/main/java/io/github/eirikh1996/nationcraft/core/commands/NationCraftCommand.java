package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.core.Core;

import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.api.objects.TextColor.*;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationCraftCommand extends Command {


    NationCraftCommand() {
        super("nationcraft", Arrays.asList("nc", "ncraft"));
    }

    private void defaultCommand(NCCommandSender sender){
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if (!name.equalsIgnoreCase("nationcraft")){
            return;
        }
        if (args.length == 0) {
            final NationCraftMain main = Core.getMain();
            final String[] message = {
                    DARK_GRAY + "====================={" + AQUA + "Nation" + GRAY + "Craft" + DARK_GRAY + "}=====================",
                    DARK_AQUA + "Version: " + main.getVersion(),
                    DARK_AQUA + "Authors: " + String.join(", ", main.getAuthors()),
                    DARK_AQUA + "Type /nationcraft help for command help",
                    DARK_GRAY + "========================================================================="
            } ;
            sender.sendMessage(message);
            return;
        }
        if (args[0].equalsIgnoreCase("convertfromfactions")){

        }
    }
}
