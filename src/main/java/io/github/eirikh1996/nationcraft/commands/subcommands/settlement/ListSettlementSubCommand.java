package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.utils.TopicPaginator;
import org.bukkit.entity.Player;

public class ListSettlementSubCommand extends SettlementSubCommand {
    private final int page;
    public ListSettlementSubCommand(Player sender, int page) {
        super(sender);
        this.page = page;
    }

    @Override
    public void execute() {
        TopicPaginator paginator = new TopicPaginator("Settlements");
        for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
            if (s == null)
                continue;
            paginator.addLine(s.getName() + " [" + s.getTownCenter().getCenterPoint().getBlockX() + ", "+ s.getTownCenter().getCenterPoint().getBlockX() + "]");

        }
        String[] pages = paginator.getPage(page);
        for (String line : pages){
            sender.sendMessage(line);
        }
    }
}
