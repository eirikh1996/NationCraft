package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.nation.Ranks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationInviteCommand extends Command {

    public NationInviteCommand(){
        super("invite", Arrays.asList("i"));

    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        NCPlayer np = (NCPlayer) sender;
        Nation n = np.getNation();

        if (n == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }

        if (!PlayerManager.getInstance().playerIsAtLeast(np, Ranks.OFFICER)) {
            sender.sendMessage("You must be at least officer to invite players");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage("You need to specify player");
            return;
        }
        NCPlayer target = PlayerManager.getInstance().getPlayer(args[0]);
        if (target != null) {
            target.sendMessage(
                    Component
                            .text("You have been invited to join ")
                            .append(n.getName(target))
                            .append(Component.space())
                            .append(
                                    Component.text("[Accept]", NamedTextColor.AQUA)
                                            .clickEvent(ClickEvent.runCommand("/nation join " + n.getName()))
                                            .hoverEvent(HoverEvent.showText(Component.text("Click to join " + n.getName(), NamedTextColor.AQUA)))
                            )
                    );
        } else {
            sender.sendMessage(Messages.ERROR + "Player " + args[0] + " has never joined the server!");
            return;
        }
        n.invite(np);
    }

    @Override
    public List<String> getTabCompletions(final NCCommandSender sender, final String[] args) {
        final ArrayList<String> completions = new ArrayList<>();
        for (NCPlayer player : PlayerManager.getInstance()) {
            completions.add(player.getName());
        }
        return completions;
    }
}
