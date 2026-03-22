package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.utils.TopicPaginator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementListCommand extends Command {

    public SettlementListCommand() {
        super("list");
        addParameter("page", new IntegerParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        TopicPaginator paginator = new TopicPaginator("Settlements");
        for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
            if (s == null)
                continue;
            paginator.addLine(
                    Component.text(s.getName() + " [" + s.getTownCenter().getCenterPoint().getBlockX() + ", "+ s.getTownCenter().getCenterPoint().getBlockZ() + "]", NamedTextColor.DARK_AQUA)
                    );

        }
        int page = Math.max(getParameter("page").getValue(), 1);
        if (!paginator.isInBounds(page)) {
            return;
        }
        TextComponent[] pages = paginator.getPage(page, "/settlement list ");
        for (TextComponent line : pages){
            player.sendMessage(line);
        }
    }
}
