package io.github.eirikh1996.nationcraft.core.commands;

public class NationCraftCommandException extends RuntimeException {
    public NationCraftCommandException(String message) {
        super(message);
    }

    public NationCraftCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
