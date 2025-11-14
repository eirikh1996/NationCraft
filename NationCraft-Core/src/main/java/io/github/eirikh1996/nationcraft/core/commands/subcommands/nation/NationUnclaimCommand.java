package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.territory.Territory;

import java.util.*;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationUnclaimCommand extends Command {

    public NationUnclaimCommand(){
        super("unclaim");
    }


    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
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
        sender.sendMessage(nationName);
        final NationManager manager = NationManager.getInstance();
        Set<Territory> unclaimedTerritory = new HashSet<>();
        if (nationName.length() > 0) {
            if (!sender.hasPermission("nationcraft.nation.claim.other")) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You can only claim for your own nation.");
                return;
            }
            nation = manager.getNationByName(nationName);
        } else {
            nation = manager.getNationByPlayer(player);
        }
        if (nation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You are not in a nation!");
            return;
        }

        nation.unclaimTerritory(player, shape, radius);
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
