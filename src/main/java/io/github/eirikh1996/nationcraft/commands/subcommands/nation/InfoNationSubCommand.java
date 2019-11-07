package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class InfoNationSubCommand extends NationSubCommand {
    private final String name;
    public InfoNationSubCommand(Player sender, String name){
        super(sender);
        this.name = name;
    }

    public InfoNationSubCommand(Player sender){
        super(sender);
        name = "";
    }
    @Override
    public void execute() {
        Nation n;
        if (name.length() == 0) {
            n = NationManager.getInstance().getNationByPlayer(sender.getUniqueId());
        } else {
            n = NationManager.getInstance().getNationByName(name);
        }
        if (n == null) {
            sender.sendMessage(Messages.ERROR + "Nation does not exist!");
            return;
        }
        ChatColor color = NationManager.getInstance().getColor(sender, n);
        Messages.nationInfo(n, sender, color);
    }
}
