package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.utils.TopicPaginator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListNationSubCommand extends NationSubCommand {
    private final int page;
    public ListNationSubCommand(Player p, int page){
        super(p);
        this.page = page;
    }
    public ListNationSubCommand(Player p){
        super(p);
        page = 1;
    }
    @Override
    public void execute() {
        if (NationManager.getInstance().getNations().isEmpty()) {
            sender.sendMessage(Messages.ERROR + "No nations found");
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Nation list");
        for (Nation n : NationManager.getInstance()) {
            String nationName = NationManager.getInstance().getColor(sender, n) + n.getName() + ChatColor.YELLOW;
            int settlements = n.getSettlements() != null ? n.getSettlements().size() : 0;
            paginator.addLine(String.format("%s: Players: %d, Settlements: %d, Capital: %s", nationName, n.getPlayers().keySet().size(), settlements, n.getCapital()));
        }
        for (String msg : paginator.getPage(page)) {
            sender.sendMessage(msg);
        }
    }
}
