package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.text.*;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

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
            ChatText.Builder builder = ChatText.builder();
            List<String> text = new ArrayList<>();
            int index = 0;
            int max = NationManager.getInstance().getRegisteredFlags().size() - 1;
            for (String flag : NationManager.getInstance().getRegisteredFlags().keySet()) {
                TextColor color = target.hasFlag(flag) ? (target.getFlag(flag) ? TextColor.GREEN : TextColor.RED) : TextColor.DARK_RED;
                if (!(sender instanceof NCPlayer)) {
                    text.add(color + flag);
                    continue;
                }
                builder.addText(color, flag + (index < max ? ", " : ""), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation flag set " + flag + !target.getFlag(flag)), new HoverEvent(HoverEvent.Action.SHOW_TEXT, "Click to set this flag to " + !target.getFlag(flag)));
                index++;
            }

            if (!(sender instanceof NCPlayer)) {
                sender.sendMessage(target.getName() + "'s flags: ");
                sender.sendMessage(String.join(", ", text));
                return;
            }
            sender.sendMessage(target.getName((NCPlayer) sender) + TextColor.RESET + "'s flags: ");
            ((NCPlayer) sender).sendMessage(builder.build());
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
