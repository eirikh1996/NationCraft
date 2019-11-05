package io.github.eirikh1996.nationcraft.commands.subcommands.nationcraft;

import io.github.eirikh1996.nationcraft.commands.subcommands.SubCommand;
import org.bukkit.entity.Player;

public abstract class NationCraftSubCommand extends SubCommand {

    protected NationCraftSubCommand(Player sender) {
        super(sender);
    }
}
