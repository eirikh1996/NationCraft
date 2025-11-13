package io.github.eirikh1996.nationcraft.sponge.objects;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.block.entity.CommandBlock;

public class NCSpongeBlockSender implements NCCommandSender {
    private final CommandBlock commandBlock;

    public NCSpongeBlockSender(CommandBlock commandBlock) {
        this.commandBlock = commandBlock;
    }

    @Override
    public void sendMessage(Component text) {
        commandBlock.sendMessage(text);
    }

    @Override
    public boolean hasPermission(String permission) {
        return commandBlock.hasPermission(permission);
    }
}
