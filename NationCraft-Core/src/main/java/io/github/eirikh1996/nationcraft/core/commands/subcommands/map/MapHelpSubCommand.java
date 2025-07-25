package io.github.eirikh1996.nationcraft.core.commands.subcommands.map;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;

public class MapHelpSubCommand extends Command {
    public MapHelpSubCommand() {
        super("help", "?");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        final NCPlayer p = (NCPlayer) sender;
        p.sendMessage(Component.text("=================== Map help ==================="));
        p.sendMessage(Component.text("Enemy", NamedTextColor.RED));
        p.sendMessage(Component.text("Neutral", NamedTextColor.WHITE));
        p.sendMessage(Component.text("Ally", NamedTextColor.DARK_PURPLE));
        p.sendMessage(Component.text("Safezone", NamedTextColor.GOLD));
        p.sendMessage(Component.text("Warzone", NamedTextColor.DARK_RED));    }
}
