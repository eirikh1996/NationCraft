package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.utils.TopicPaginator;

public class SettlementListCommand extends Command {

    public SettlementListCommand() {
        super("list");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        TopicPaginator paginator = new TopicPaginator("Settlements");
        for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
            if (s == null)
                continue;
            paginator.addLine(s.getName() + " [" + s.getTownCenter().getCenterPoint().getBlockX() + ", "+ s.getTownCenter().getCenterPoint().getBlockX() + "]");

        }
        int page = args.length == 0 ? 0 : Integer.parseInt(args[0]);
        if (!paginator.isInBounds(page)) {
            return;
        }
        String[] pages = paginator.getPage(page);
        for (String line : pages){
            sender.sendMessage(line);
        }
    }
}
