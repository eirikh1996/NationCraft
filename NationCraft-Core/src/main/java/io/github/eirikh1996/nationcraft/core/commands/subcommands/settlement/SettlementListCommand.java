package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.utils.TopicPaginator;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementListCommand extends Command {

    public SettlementListCommand() {
        super("list");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Settlements");
        NCPlayer player = (NCPlayer) sender;
        for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
            if (s == null)
                continue;
            paginator.addLine(ChatText.builder()
                    .addText(TextColor.DARK_AQUA, s.getName() + " [" + s.getTownCenter().getCenterPoint().getBlockX() + ", "+ s.getTownCenter().getCenterPoint().getBlockX() + "]")
                    .build());

        }
        int page = args.length == 0 ? 1 : Integer.parseInt(args[0]);
        if (!paginator.isInBounds(page)) {
            return;
        }
        ChatText[] pages = paginator.getPage(page, "/settlement list ");
        for (ChatText line : pages){
            player.sendMessage(line);
        }
    }
}
