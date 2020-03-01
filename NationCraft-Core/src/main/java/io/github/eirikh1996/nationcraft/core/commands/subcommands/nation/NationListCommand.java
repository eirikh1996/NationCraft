package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.objects.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.utils.TopicPaginator;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationListCommand extends Command {
    public NationListCommand(){
        super("list");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        int page = args.length == 0 ? 0 : Integer.parseInt(args[0]);
        if (NationManager.getInstance().getNations().isEmpty()) {
            sender.sendMessage(Messages.ERROR + "No nations found");
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Nation list");
        for (Nation n : NationManager.getInstance()) {
            String nationName = n.getName(player) + TextColor.YELLOW;
            int settlements = n.getSettlements() != null ? n.getSettlements().size() : 0;
            paginator.addLine(String.format("%s: Players: %d, Settlements: %d, Capital: %s", nationName, n.getPlayers().keySet().size(), settlements, n.getCapital()));
        }
        if (!paginator.isInBounds(page)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid page: " + page);
        }
        for (String msg : paginator.getPage(page)) {
            sender.sendMessage(msg);
        }
    }
}
