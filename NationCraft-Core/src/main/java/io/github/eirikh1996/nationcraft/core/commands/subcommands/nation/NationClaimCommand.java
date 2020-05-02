package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.Core;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationClaimCommand extends Command {
    public NationClaimCommand(){
        super("claim", Arrays.asList("c"));
        argument = String.join(", ", Shape.getShapeNames()) + " [radius] [nation]";
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        final Nation nation;
        final Shape shape = args.length > 0 ? Shape.getShape(args[0]) : Shape.SINGLE;
        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (Exception e) {
            radius = 0;
        }
        String nationName;
        try {
            nationName = radius > 0 ? args[2] : args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            nationName = "";
        }

        NCPlayer player = (NCPlayer) sender;
        if (nationName.length() > 0) {
            if (!sender.hasPermission("nationcraft.nation.claim.other")) {
                sender.sendMessage(Messages.ERROR + "You can only claim for your own nation.");
                return;
            }
            Core.getMain().broadcast(nationName);
            nation = NationManager.getInstance().getNationByName(nationName);
        } else {
            nation = NationManager.getInstance().getNationByPlayer(player.getPlayerID());
        }
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }

        if (shape == Shape.SINGLE) {
            nation.getTerritoryManager().claimSignleTerritory(player);
        } else if (shape == Shape.CIRCLE) {
            nation.getTerritoryManager().claimCircularTerritory(player, radius);
        } else if (shape.equals(Shape.SQUARE)) {
            nation.getTerritoryManager().claimSquareTerritory(player, radius);
        } else if (shape.equals(Shape.LINE)) {
            nation.getTerritoryManager().claimLineTerritory(player, radius);
        }
        nation.saveToFile();
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
