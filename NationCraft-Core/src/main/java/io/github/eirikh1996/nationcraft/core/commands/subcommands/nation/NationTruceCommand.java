package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.ClickEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.HoverEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

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
            truce.broadcast(ChatText.builder()
                    .addText(ownNation.getName(truce) + "§r wants to be in truce with your nation. ")
                    .addText(TextColor.GREEN + "[Accept]",
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation truce " + ownNation.getName()),
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, "Click to accept truce"))
                    .build());
            ownNation.broadcast(player.getName() + " informed nation " + truce.getName(ownNation) + " that your nation wants to be in truce");
            return;
        }
        truce.broadcast(ownNation.getName(truce) + " is now a nation in truce");
        ownNation.broadcast(truce.getName(ownNation) + " is now a nation in truce");

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
