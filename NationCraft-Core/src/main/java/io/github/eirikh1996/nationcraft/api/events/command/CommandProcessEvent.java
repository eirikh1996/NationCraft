package io.github.eirikh1996.nationcraft.api.events.command;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.events.Event;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

public class CommandProcessEvent extends Event implements Cancellable {

    private final NCCommandSender sender;
    private final Command command;

    private boolean cancelled = false;

    public CommandProcessEvent(NCCommandSender sender, Command command) {
        this.sender = sender;
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public NCCommandSender getSender() {
        return sender;
    }
}
