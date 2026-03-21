package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class Parameter<T> {
    private final ParameterType<T> type;
    private final String name;
    private final boolean required;
    private T value;

    public Parameter(ParameterType<T> type, String name, boolean required) {
        this.type = type;
        this.name = name;
        this.required = required;
    }

    public Parameter(ParameterType<T> type, String name) {
        this(type, name, false);
    }

    public void parse(@NotNull NCCommandSender sender, @NotNull String input) {
        T value = type.readArgument(sender, input);
        if (value == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(type.getErrorMessage()).append(Component.text(input)));
            return;
        }
        this.value = value;
    }

    public ParameterType<?> getType() {
        return type;
    }

    public <T> T getValue() {
        return (T) value;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Parameter<?> param)) {
            return false;
        }
        return name.equals(param.name);
    }
}
