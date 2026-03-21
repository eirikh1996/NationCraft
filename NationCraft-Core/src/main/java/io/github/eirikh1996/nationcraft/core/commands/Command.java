package io.github.eirikh1996.nationcraft.core.commands;


import io.github.eirikh1996.nationcraft.core.commands.parameters.Parameter;
import io.github.eirikh1996.nationcraft.core.commands.parameters.ParameterType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Command {
    protected final String name;
    protected String permission = "";
    protected String argument = "";
    protected Command parent;
    protected final List<String> aliases;
    protected final Map<String, Command> children;
    private final Map<String, Parameter<?>> parameters = new HashMap<>();
    private final List<String> parameterIndices = new ArrayList<>();


    protected Command(String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
        children = new HashMap<>();
    }

    protected Command(String name, String... aliases) {
        this.name = name;
        this.aliases = new ArrayList<>();
        this.aliases.addAll(Arrays.asList(aliases));
        children = new HashMap<>();
    }

    protected Command(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        children = new HashMap<>();
    }

    protected abstract void execute(NCCommandSender sender);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addChild(Command cmd) {
        cmd.parent = this;
        children.put(cmd.getName(), cmd);
        if (cmd.getAliases().isEmpty()) {
            return;
        }
        for (String alias : cmd.getAliases()) {
            children.put(alias, cmd);
        }
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    protected void addParameter(@NotNull String name, @NotNull ParameterType<?> type, boolean required) {
        name = name.toLowerCase();
        if (parameters.containsKey(name)) {
            throw new NationCraftCommandException("Parameter " + name + " is already registered with command " + this.name);
        }
        final Parameter<?> arg = new Parameter<>(type, name, required);
        parameters.put(name, arg);
        parameterIndices.add(name);
    }

    protected void addParameter(@NotNull String name, @NotNull ParameterType<?> type) {
        addParameter(name, type, false);
    }

    public Map<String, Command> getChildren() {
        return children;
    }

    public String getArgument() {
        return argument;
    }

    public Parameter<?> getParameter(@NotNull String name) {
        name = name.toLowerCase();
        if (!parameters.containsKey(name)) {
            throw new NationCraftCommandException("Invalid parameter: " + name);
        }
        return parameters.get(name);
    }

    public Parameter<?> getParameterByIndex(int index) {
        String paramName = parameterIndices.get(index);
        return parameters.get(paramName);
    }

    /**
     *
     * Used to validate if a command sender is allowed to execute a command
     *
     * @param sender The command sender to validate
     * @return true if it is a valid command sender and executed as base method, false otherwise and if overridden by a subclass
     */
    protected boolean validateCommandSender(NCCommandSender sender) {
        return true;
    }

    public int getRegisteredParameters() {
        return parameters.size();
    }

    public Optional<Command> getChild(String childName) {
        return Optional.ofNullable(children.getOrDefault(childName, null));
    }

    public List<String> getTabCompletions(final NCCommandSender sender, final String[] args) {
        List<String> completions = new ArrayList<>();
        for (Map.Entry<String, Command> entry : children.entrySet()) {
            completions.add(entry.getKey());
        }
        return completions;
    }
    public boolean isParent() {
        return parent != null;
    }

    public static List<String> aliases(String... aliases) {
        return Arrays.asList(aliases);
    }

    public String getPermission() {
        return permission;
    }
}
