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

public final class NationNeutralCommand extends Command {

    public NationNeutralCommand(){
        super("neutral");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You must specify a nation");
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        NationManager nMgr = NationManager.getInstance();
        Nation ownNation = nMgr.getNationByPlayer(player); //sender's own nation
        Nation neutralNation = nMgr.getNationByName(args[0]); //nation to neutral
        //Do not set relation wish with sender's own nation
        if (ownNation == neutralNation) {
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You cannot set relation to your own nation!");
            return;
        }
        if (!ownNation.getAllies().contains(neutralNation) && !ownNation.getEnemies().contains(neutralNation) && !neutralNation.getEnemies().contains(ownNation) && !ownNation.getTruces().contains(neutralNation)) {
            player.sendMessage(
                    NATIONCRAFT_COMMAND_PREFIX
                            .append(neutralNation.getName(ownNation))
                            .append(Component.text(" is already neutral")));
            return;
        }
        if (ownNation.getAllies().contains(neutralNation)) {
            ownNation.removeAlly(neutralNation);
        }
        if (ownNation.getTruces().contains(neutralNation)) {
            ownNation.removeTruce(neutralNation);
        }
        if (ownNation.getEnemies().contains(neutralNation)) {
            ownNation.removeEnemy(neutralNation);
        }
        //if the sender's nation is on the neutral nations enemy list
        if (neutralNation.getEnemies().contains(ownNation)) {
            ownNation.broadcast(player.getName() + " has informed " + neutralNation.getName(ownNation) + " that this nation wants to be neutral");
            neutralNation.broadcast(
                ownNation.getName(neutralNation)
                    .append(Component.text(" wants to be neutral with your nation. "))
                    .append(Component
                            .text("[Accept]", NamedTextColor.GREEN)
                            .clickEvent(ClickEvent.runCommand("/nation neutral " + ownNation.getName()))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to accept neutrality")))
                    )

                );
            ownNation.removeEnemy(neutralNation);
            return;
        }
        ownNation.broadcast(neutralNation.getName() + " is now a neutral nation");
        neutralNation.broadcast(ownNation.getName() + " is now a neutral nation");
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
