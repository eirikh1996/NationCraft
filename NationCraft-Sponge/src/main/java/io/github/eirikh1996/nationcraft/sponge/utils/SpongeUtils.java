package io.github.eirikh1996.nationcraft.sponge.utils;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.sponge.objects.NCSpongeBlockSender;
import io.github.eirikh1996.nationcraft.sponge.objects.NCSpongeConsole;
import io.github.eirikh1996.nationcraft.sponge.player.SpongePlayerManager;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.Server;
import org.spongepowered.api.block.entity.CommandBlock;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeUtils {

    public NCCommandSender getCorrespondingSender(Audience sender) {
        if (sender instanceof Player player) {
            return SpongePlayerManager.getInstance().getPlayer(player.uniqueId());
        } else if (sender instanceof Server server) {
            return new NCSpongeConsole(server);
        } else if (sender instanceof CommandBlock commandBlock) {
            return new NCSpongeBlockSender(commandBlock);
        } else {
            throw new IllegalStateException("Invalid type: " + sender.getClass());
        }
    }
}
