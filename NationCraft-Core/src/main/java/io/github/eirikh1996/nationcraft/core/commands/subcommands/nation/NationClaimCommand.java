package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.ShapeParameterType;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationClaimCommand extends Command {
    public NationClaimCommand(){
        super("claim", List.of("c"));
        addParameter("shape", new ShapeParameterType());
        addParameter("radius", new IntegerParameterType());
        addParameter("nation", new NationParameterType());
        argument = String.join(", ", Shape.getShapeNames()) + " [radius] [nation]";
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final Nation nation = getParameter("nation").getValue();
        final Shape shape = getParameter("shape").getValue();
        int radius = getParameter("radius").getValue();
        if (player.getNation() != nation && !sender.hasPermission("nationcraft.nation.claim.other")) {
                sender.sendMessage(Messages.ERROR.append(Component.text("You can only claim for your own nation.")));
                return;
            }
            //Core.getMain().broadcast(nationName);
            //nation = NationManager.getInstance().getNationByName(nationName);
        if (nation == null) {
            sender.sendMessage(Messages.ERROR.append(Component.text("You are not in a nation!")));
            return;
        }
        nation.claimTerritory(player, shape, radius);
    }

    @Override
    public List<String> getTabCompletions(final NCCommandSender sender, final String[] args) {
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
