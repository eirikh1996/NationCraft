package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.ClickEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.HoverEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationAllyCommand extends Command {
    public NationAllyCommand(){
        super("ally", Arrays.asList("a"));
        argument = "<nation>";
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (!sender.hasPermission("nationcraft.nation.ally")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + NO_PERMISSION);
            return;
        }

        NationManager nMgr = NationManager.getInstance();
        final NCPlayer player = (NCPlayer) sender;
        Nation ownNation = player.getNation(); //sender's own nation
        if (ownNation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + NOT_IN_A_NATION);
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a nation");
            return;
        }
        Nation allyNation = nMgr.getNationByName(args[0]); //nation to ally
        if (allyNation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "The given nation does not exist");
            return;
        }
        if (ownNation.isAlliedWith(allyNation)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You have already set this relation wish with " + allyNation.getName());
            return;
        }
        ownNation.addAlly(allyNation);
        if (allyNation.getAllies().contains(ownNation)) {
            ownNation.broadcast(allyNation.getName(ownNation) + " is now an allied nation");
            allyNation.broadcast(ownNation.getName(allyNation) + " is now an allied nation");
            return;
        }
        allyNation.broadcast(ChatText.builder()
        .addText(NATIONCRAFT_COMMAND_PREFIX + ownNation.getName(allyNation) + " §r wishes to be an allied nation. ")
        .addText(TextColor.DARK_GREEN + "[Accept]",
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation ally " + ownNation.getName()),
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextColor.GREEN + "Click to accept the alliance"))
        .build());
        ownNation.broadcast(player.getName() + " informed nation " + allyNation.getName(ownNation) + " that you wish to be an allied nation");


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
