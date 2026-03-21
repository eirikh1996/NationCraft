package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.TextComponent;

public class ShapeParameterType extends AbstractParameterType<Shape> {
    public ShapeParameterType() {
        super(Shape.class);
    }

    @Override
    public Shape readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty()) {
            return Shape.SINGLE;
        }
        return Shape.getShape(input);
    }
}
