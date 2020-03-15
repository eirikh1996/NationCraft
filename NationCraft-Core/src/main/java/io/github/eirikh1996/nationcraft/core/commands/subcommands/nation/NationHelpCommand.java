package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.utils.TopicPaginator;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationHelpCommand extends Command {

    public NationHelpCommand() {
        super("help", Arrays.asList("h", "?"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Nation commands");
        int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        if (sender.hasPermission("nationcraft.nation.create"))
            paginator.addLine(ChatText.builder().addText("/nation,n create <nation name>").build());
        if (sender.hasPermission("nationcraft.nation.join"))
            paginator.addLine(ChatText.builder().addText("/nation,n join <nation name>").build());
        if (sender.hasPermission("nationcraft.nation.leave"))
            paginator.addLine(ChatText.builder().addText("/nation,n leave").build());
        if (sender.hasPermission("nationcraft.nation.info"))
            paginator.addLine(ChatText.builder().addText("/nation,n info").build());
        if (sender.hasPermission("nationcraft.nation.disband"))
            paginator.addLine(ChatText.builder().addText("/nation,n disband").build());
        if (sender.hasPermission("nationcraft.nation.kick"))
            paginator.addLine(ChatText.builder().addText("/nation,n kick <player>").build());
        if (sender.hasPermission("nationcraft.nation.claim"))
            paginator.addLine(ChatText.builder().addText("/nation,n claim <circle,line,square,single> " + (sender.hasPermission("nationcraft.nation.claim.other") ? "[nation name]" : "")).build());
        if (sender.hasPermission("nationcraft.nation.invite"))
            paginator.addLine(ChatText.builder().addText("/nation,n invite <player>").build());
        if (sender.hasPermission("nationcraft.nation.list"))
            paginator.addLine(ChatText.builder().addText("/nation,n list [page number]").build());
        if (sender.hasPermission("nationcraft.nation.unclaim"))
            paginator.addLine(ChatText.builder().addText("/nation,n unclaim").build());
        if (sender.hasPermission("nationcraft.nation.help"))
            paginator.addLine(ChatText.builder().addText("/nation,n help").build());
        for (ChatText line : paginator.getPage(page, "/nation help ")){
            ((NCPlayer)sender).sendMessage(line);
        }
    }
}
