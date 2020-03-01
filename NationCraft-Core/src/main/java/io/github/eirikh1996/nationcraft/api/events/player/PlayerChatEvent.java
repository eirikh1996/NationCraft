package io.github.eirikh1996.nationcraft.api.events.player;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;

import java.util.Set;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {

    private String format;
    private String message;
    private Set<NCPlayer> recipients;
    public PlayerChatEvent(final NCPlayer player, String format, String message, Set<NCPlayer> recipients) {
        super(player);
        this.format = format;
        this.message = message;
        this.recipients = recipients;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<NCPlayer> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<NCPlayer> recipients) {
        this.recipients = recipients;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }
}
