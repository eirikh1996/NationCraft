package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationFlagListCommand extends Command {
    protected NationFlagListCommand() {
        super("list");
        addParameter("nation", new NationParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        Nation target = getParameter("nation").getValue();
        if (target == null) {
            //sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text((args.length > 1 ? "Invalid nation name: " + args[1] : "You must specify a nation"))));
            return;
        }
        TextComponent text = Component.empty();
        int index = 0;
        int max = NationManager.getInstance().getRegisteredFlags().size() - 1;
        for (String flag : NationManager.getInstance().getRegisteredFlags().keySet()) {
            NamedTextColor color = target.hasFlag(flag) ? (target.getFlag(flag) ? NamedTextColor.GREEN : NamedTextColor.RED) : NamedTextColor.DARK_RED;
            if (!(sender instanceof NCPlayer)) {
                text = text.append(Component.text(flag, color));
                continue;
            }
            text = text.append(
                    Component.text(flag, color)
                            .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/nation flag set " + flag + !target.getFlag(flag)))
                            .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click to set this flag to " + !target.getFlag(flag)))));
            if (index < max)
                text = text.append(Component.text(", "));
            //builder.addText(color, flag + (index < max ? ", " : ""), new ClickEvent(ClickEvent.Action.RUN_COMMAND, , new HoverEvent(HoverEvent.Action.SHOW_TEXT, ));
            index++;
        }

        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(target.getName() + "'s flags: ");
            sender.sendMessage(text);
            return;
        }
        sender.sendMessage(target.getName((NCPlayer) sender).append(Component.text("'s flags: ")));
        sender.sendMessage(text);
    }
}
