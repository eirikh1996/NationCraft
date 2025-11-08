package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.Core;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.CommandRegistry;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.utils.TopicPaginator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.Arrays;
import java.util.Map;

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
        for (Map.Entry<String, Command> entry : parent.getChildren().entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(entry.getValue().getName()))
                continue;
            if (!sender.hasPermission("nationcraft.nation." + entry.getKey().toLowerCase()))
                continue;
            paginator.addLine(Component.text("/nation, n " + entry.getKey() + " " + entry.getValue().getArgument()));
        }
        for (TextComponent line : paginator.getPage(page, "/nation help ")){
            sender.sendMessage(line);
        }
    }
}
