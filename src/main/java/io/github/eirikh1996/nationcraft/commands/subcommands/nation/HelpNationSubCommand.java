package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.utils.TopicPaginator;
import org.bukkit.entity.Player;

public final class HelpNationSubCommand extends NationSubCommand {
    private final int page;
    public HelpNationSubCommand(Player sender, int page) {
        super(sender);
        this.page = page;
    }

    @Override
    public void execute() {
        TopicPaginator paginator = new TopicPaginator("Nation commands");
        if (sender.hasPermission("nationcraft.nation.create"))
            paginator.addLine("/nation,n create <nation name>");
        if (sender.hasPermission("nationcraft.nation.join"))
            paginator.addLine("/nation,n join <nation name>");
        if (sender.hasPermission("nationcraft.nation.leave"))
            paginator.addLine("/nation,n leave");
        if (sender.hasPermission("nationcraft.nation.info"))
            paginator.addLine("/nation,n info");
        if (sender.hasPermission("nationcraft.nation.disband"))
            paginator.addLine("/nation,n disband");
        if (sender.hasPermission("nationcraft.nation.kick"))
            paginator.addLine("/nation,n kick <player>");
        if (sender.hasPermission("nationcraft.nation.claim"))
            paginator.addLine("/nation,n claim <circle,line,square,single> " + (sender.hasPermission("nationcraft.nation.claim.other") ? "[nation name]" : ""));
        if (sender.hasPermission("nationcraft.nation.invite"))
            paginator.addLine("/nation,n invite <player>");
        if (sender.hasPermission("nationcraft.nation.list"))
            paginator.addLine("/nation,n list [page number]");
        if (sender.hasPermission("nationcraft.nation.unclaim"))
            paginator.addLine("/nation,n unclaim");
        if (sender.hasPermission("nationcraft.nation.help"))
            paginator.addLine("/nation,n help");
        for (String line : paginator.getPage(page)){
            sender.sendMessage(line);
        }
    }
}
