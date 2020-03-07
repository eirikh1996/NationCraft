package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.territory.Territory;
import io.github.eirikh1996.nationcraft.core.utils.TopicPaginator;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationTerritoryCommand extends Command {
    public NationTerritoryCommand() {
        super("territory");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
        }
        final NCPlayer player = (NCPlayer) sender;
        Nation n = args.length > 1 ? NationManager.getInstance().getNationByName(args[1]) : player.getNation();
        int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        TopicPaginator paginator = new TopicPaginator("Territory");
        for (Territory territory : n.getTerritoryManager()) {
            paginator.addLine(territory.toString());
        }
        if (!paginator.isInBounds(page)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid page: " + page);
            return;
        }
        sender.sendMessage(paginator.getPage(page));
    }
}
