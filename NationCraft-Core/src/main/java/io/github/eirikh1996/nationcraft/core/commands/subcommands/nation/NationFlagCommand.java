package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.text.*;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationFlagCommand extends Command {
    public NationFlagCommand() {
        super("flag");
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Usage: /nation flag <list|set>");
        } else if (args[0].equalsIgnoreCase("list")) {
            Nation target = args.length > 1 ? NationManager.getInstance().getNationByName(args[1]) : (sender instanceof NCPlayer ? ((NCPlayer) sender).getNation() : null);
            if (target == null) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + (args.length > 1 ? "Invalid nation name: " + args[1] : "You must specify a nation"));
                return;
            }
            TextComponent text = Component.empty();
            int index = 0;
            int max = NationManager.getInstance().getRegisteredFlags().size() - 1;
            for (String flag : NationManager.getInstance().getRegisteredFlags().keySet()) {
                NamedTextColor color = target.hasFlag(flag) ? (target.getFlag(flag) ? NamedTextColor.GREEN : NamedTextColor.RED) : NamedTextColor.DARK_RED;
                if (!(sender instanceof NCPlayer)) {
                    text.append(Component.text(flag, color));
                    continue;
                }
                text.append(
                        Component.text(flag, color)
                                .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/nation flag set " + flag + !target.getFlag(flag)))
                                .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click to set this flag to " + !target.getFlag(flag)))));
                if (index < max)
                    text.append(Component.text(", "));
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
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You need to specify a flag");
                return;
            }
            String flag = args[1];
            if (!NationManager.getInstance().registeredFlag(flag)) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid flag: " + flag);
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify either true or false");
                return;
            }
            boolean flagValue;
            if (args[2].equalsIgnoreCase("true")) {
                flagValue = true;
            } else if (args[2].equalsIgnoreCase("false")) {
                flagValue = false;
            } else {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify either true or false");
                return;
            }

            Nation target = sender instanceof NCPlayer ? (args.length < 4 ? ((NCPlayer) sender).getNation() : NationManager.getInstance().getNationByName(args[3])) : null;
            if (target == null) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + (args.length > 3 ? "Invalid nation name: " + args[1] : "You must specify a nation"));
                return;
            }
            target.setFlag(flag.toLowerCase(), flagValue);
        }
    }
}
