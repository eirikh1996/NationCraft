package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public abstract class AbstractParameterType<T> implements ParameterType<T> {

    private final Class<T> typeClass;
    private final TextComponent errorMessage;

    public AbstractParameterType(Class<T> typeClass, TextComponent errorMessage) {
        this.typeClass = typeClass;
        this.errorMessage = errorMessage;
    }

    public AbstractParameterType(Class<T> typeClass) {
        this(typeClass, Component.empty());
    }

    @Override
    public Class<T> getTypeClass() {
        return typeClass;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public List<String> tabList(NCCommandSender sender, String input) {
        return List.of();
    }

    @Override
    public TextComponent getErrorMessage() {
        return errorMessage;
    }
}
