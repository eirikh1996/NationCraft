package io.github.eirikh1996.nationcraft.api;

import io.github.eirikh1996.nationcraft.api.events.*;
import io.github.eirikh1996.nationcraft.api.events.listener.NCListener;
import io.github.eirikh1996.nationcraft.core.Core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class NationCraftAPI {
    final Thread serverThread;
    private static NationCraftAPI instance;

    private NationCraftAPI() {
        serverThread = Thread.currentThread();
    }
    Set<NCListener> listeners = new HashSet<>();
    Set<Object> commandExecutors = new HashSet<>();

    public void registerEvent(NCListener listener) {
        Core.getMain().logInfo("Registered listener " + listener.getClass().getName() + " to NationCraft API");
        listeners.add(listener);
    }

    public void unregisterEvent(NCListener listener) {
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
                    if (event instanceof Cancellable) {
                        Cancellable can = (Cancellable) event;
                        if (method.getAnnotation(EventListener.class).ignoreCancelled() && can.isCancelled()) {
                            continue;
                        }
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
