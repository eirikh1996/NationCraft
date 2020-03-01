package io.github.eirikh1996.nationcraft.api;

import io.github.eirikh1996.nationcraft.api.events.Event;
import io.github.eirikh1996.nationcraft.api.events.EventException;
import io.github.eirikh1996.nationcraft.api.events.EventListener;
import io.github.eirikh1996.nationcraft.api.events.Priority;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.Executor;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationCraftAPI {
    final Thread serverThread;
    private static NationCraftAPI instance;

    private NationCraftAPI() {
        serverThread = Thread.currentThread();
    }
    Set<Object> listeners = new HashSet<>();
    Set<Object> commandExecutors = new HashSet<>();

    public void registerEvent(Object listener) {
        listeners.add(listener);
    }

    public void unregisterEvent(Object listener) {
        listeners.remove(listener);
    }

    public <E extends Event> E callEvent(E event) {
        for (Priority priority : Priority.values()) {
            for (Object listener : listeners) {
                for (Method method : listener.getClass().getMethods()) {
                    if (!method.isAnnotationPresent(EventListener.class)) {
                        continue;
                    }
                    if (method.getAnnotation(EventListener.class).priority() != priority) {
                        continue;
                    }
                    if (!method.getParameters()[0].getType().isAssignableFrom(event.getClass())) {
                        continue;
                    }
                    if (Thread.currentThread() != serverThread && !event.isAsync()) {
                        throw new IllegalStateException(event.getClass().getName() + " cannot be fired asynchronously from another thread");
                    }
                    try {
                        method.invoke(listener, event);
                    } catch (Exception e) {
                        throw new EventException(null, e);
                    }
                }
            }
        }
        return event;
    }

    public static synchronized NationCraftAPI getInstance() {
        if (instance == null)
            instance = new NationCraftAPI();
        return instance;
    }
}
