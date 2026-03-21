package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public interface ParameterType<T> {

    String getName();

    Class<T> getTypeClass();

    T readArgument(NCCommandSender sender, String input);

    List<String> tabList(NCCommandSender sender, String input);

    TextComponent getErrorMessage();


}
