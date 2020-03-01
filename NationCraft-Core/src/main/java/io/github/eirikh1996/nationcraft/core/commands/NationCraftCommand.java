package io.github.eirikh1996.nationcraft.core.commands;

import java.util.Arrays;
import java.util.List;

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
        if (args[0].equalsIgnoreCase("convertfromfactions")){

        }
    }
}
