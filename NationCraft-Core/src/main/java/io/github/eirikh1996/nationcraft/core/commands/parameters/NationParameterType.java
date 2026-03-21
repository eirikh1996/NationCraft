package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationParameterType extends AbstractParameterType<Nation> {
    public NationParameterType() {
        super(Nation.class, Component.text("Invalid nation name"));
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Nation readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty() && sender instanceof NCPlayer player) {
            return player.getNation();
        }
        if (input.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must specify a nation")));
            return null;
        }
        Nation nation = NationManager.getInstance().getNationByName(input);
        if (nation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Nation " + input + " does not exist")));
        }
        return nation;
    }

    @Override
    public List<String> tabList(NCCommandSender sender, String input) {
        final List<String> returnList = new ArrayList<>();
            NationManager.getInstance().forEach(nation -> {
                if (!input.isEmpty() && !nation.getName().toLowerCase().startsWith(input.toLowerCase())) {
                    return;
                }
                returnList.add(nation.getName());
            });
        return returnList;
    }
}
