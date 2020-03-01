package io.github.eirikh1996.nationcraft.core.commands;


import java.util.*;

public abstract class Command {
    protected final String name;
    protected final List<String> aliases;
    protected final Map<String, Command> children;

    protected Command(String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
        children = new HashMap<>();
    }

    protected Command(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        children = new HashMap<>();
    }


    protected abstract void execute(NCCommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addChild(Command cmd) {
        children.put(cmd.getName(), cmd);
        if (cmd.getAliases().isEmpty()) {
            return;
        }
        for (String alias : cmd.getAliases()) {
            children.put(alias, cmd);
        }
    }

    public List<String> getTabCompletions() {
        List<String> completions = new ArrayList<>();
        for (Map.Entry<String, Command> entry : children.entrySet()) {
            completions.add(entry.getKey());
        }
        return completions;
    }

    public static List<String> aliases(String... aliases) {
        return Arrays.asList(aliases);
    }
}
