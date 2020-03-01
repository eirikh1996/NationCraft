package io.github.eirikh1996.nationcraft.api.events;

public abstract class Event {
    private final boolean async;

    protected Event(boolean async) {
        this.async = async;
    }

    protected Event() {
        this(false);
    }

    public boolean isAsync() {
        return async;
    }
}
