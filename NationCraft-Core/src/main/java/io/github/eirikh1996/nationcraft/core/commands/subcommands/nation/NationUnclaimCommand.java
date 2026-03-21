package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.ShapeParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.territory.Territory;

import java.util.*;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationUnclaimCommand extends Command {

    public NationUnclaimCommand(){
        super("unclaim");
        addChild(new NationUnclaimAllCommand());
        addChild(new NationUnclaimSingleCommand());
        addChild(new NationUnclaimLineCommand());
        addChild(new NationUnclaimCircleCommand());
        addChild(new NationUnclaimSquareCommand());
    }


    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList(Shape.getShapeNames());
        }
        else if (args.length == 4 && !(args[1].equalsIgnoreCase("single") || args[1].equalsIgnoreCase("s"))) {
            final ArrayList<String> completions = new ArrayList<>();
            for (Nation n : NationManager.getInstance()) {
                completions.add(n.getName());
            }
            return completions;
        } else if (args.length == 3 && (args[1].equalsIgnoreCase("single") || args[1].equalsIgnoreCase("s"))) {
            final ArrayList<String> completions = new ArrayList<>();
            for (Nation n : NationManager.getInstance()) {
                completions.add(n.getName());
            }
            return completions;
        }
        return new ArrayList<>();
    }
}
