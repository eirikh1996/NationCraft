package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.utils.TopicPaginator;

import java.util.Arrays;
import java.util.List;

public final class NationHelpCommand extends Command {

    public NationHelpCommand() {
        super("help", Arrays.asList("h", "?"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        TopicPaginator paginator = new TopicPaginator("Nation commands");
        int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
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
