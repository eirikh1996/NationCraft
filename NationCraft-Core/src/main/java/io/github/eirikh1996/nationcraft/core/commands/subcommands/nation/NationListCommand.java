package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.utils.TopicPaginator;

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
        int page = args.length == 0 ? 1 : Integer.parseInt(args[0]);
        if (NationManager.getInstance().getNations().isEmpty()) {
            sender.sendMessage(Messages.ERROR + "No nations found");
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Nation list");
        for (Nation n : NationManager.getInstance()) {
            String nationName = n.getName(player);
            int settlements = n.getSettlements().isEmpty() ? n.getSettlements().size() : 0;
            String capital = n.getCapital() == null ? "None" : n.getCapital().getName();
            paginator.addLine(ChatText.builder().addText(TextColor.DARK_AQUA, String.format("%s: Players: %d, Settlements: %d, Capital: %s", nationName, n.getPlayers().keySet().size(), settlements, capital)).build());
        }
        if (!paginator.isInBounds(page)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid page: " + page);
            return;
        }
        for (ChatText msg : paginator.getPage(page, "/nation list ")) {
            player.sendMessage(msg);
        }
    }
}
