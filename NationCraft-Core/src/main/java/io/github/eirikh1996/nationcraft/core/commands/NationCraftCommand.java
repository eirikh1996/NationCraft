package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.core.Core;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.api.objects.text.TextColor.*;

public class NationCraftCommand extends Command {


    NationCraftCommand() {
        super("nationcraft", Arrays.asList("nc", "ncraft"));
    }

    private void defaultCommand(NCCommandSender sender){
    }

    @Override
    public void execute(NCCommandSender sender) {
        if (!name.equalsIgnoreCase("nationcraft")){
            return;
        }
        final NationCraftMain main = Core.getMain();
        final String[] message = {
                DARK_GRAY + "====================={" + AQUA + "Nation" + GRAY + "Craft" + DARK_GRAY + "}=====================",
                DARK_AQUA + "Version: " + main.getVersion(),
                DARK_AQUA + "Authors: " + String.join(", ", main.getAuthors()),
                DARK_AQUA + "Type /nationcraft help for command help",
                DARK_GRAY + "========================================================================="
        } ;
        sender.sendMessage(message);
    }
}
