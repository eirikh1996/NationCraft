package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationTruceCommand extends Command {
    public NationTruceCommand() {
        super("truce");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a nation");
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        NationManager nMgr = NationManager.getInstance();
        Nation ownNation = nMgr.getNationByPlayer(player); //sender's own nation
        Nation truce = nMgr.getNationByName(args[0]); //nation to neutral
        if (truce == null) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Nation " + args[0] + " does not exist!");
            return;
        }
        if (truce == ownNation) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot set relation to your own nation!");
            return;
        }
        ownNation.addTruce(truce);
        if (!truce.getTruces().contains(ownNation) && !truce.getAllies().contains(ownNation)) {
            truce.broadcast(
                ownNation.getName(truce)
                .append(Component.text(" wants to be in truce with your nation. "))
                .append(Component.text("[Accept]", NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.runCommand("/nation truce " + ownNation.getName()))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to accept truce")))
                )
            );

            ownNation.broadcast(Component.text(player.getName())
                    .append(Component.text(" informed nation "))
                    .append(truce.getName(ownNation))
                    .append(Component.text(" that your nation wants to be in truce")));
            return;
        }
        truce.broadcast(ownNation.getName(truce).append(Component.text(" is now a nation in truce")));
        ownNation.broadcast(truce.getName(ownNation).append(Component.text(" is now a nation in truce")));

    }

    @Override
    public List<String> getTabCompletions(final NCCommandSender sender, final String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof NCPlayer)) {
            return completions;
        }
        NCPlayer player = (NCPlayer) sender;
        for (Nation n : NationManager.getInstance()) {
            if (n.equals(player.getNation()))
                continue;
            completions.add(n.getName());
        }
        return completions;
    }
}
