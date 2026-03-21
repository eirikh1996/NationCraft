package io.github.eirikh1996.nationcraft.core.commands.subcommands.player;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.DoubleParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.PlayerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.StringParameterType;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class PlayerPowerCommand extends Command {
    public PlayerPowerCommand() {
        super("power");
        addParameter("action", new StringParameterType(), true);
        addParameter("amount", new DoubleParameterType(), true);
        addParameter("player", new PlayerParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        NCPlayer target = getParameter("player").getValue();
        if (target == null && !(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Third argument must be a player name")));
            return;
        }
        if (target == null) {
            return;
        }
        String action = getParameter("action").getValue();
        if (action.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Required syntax is /player power <add|set|remove|reset> [target player]")));
            return;
        }
        double amount = getParameter("amount").getValue();
        if (action.equalsIgnoreCase("set")) {
            if (amount <= -1) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must supply a value between 0 and " + Settings.player.MaxPower)));
                return;
            }
            if (amount > Settings.player.MaxPower) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Supplied number is greater than max power of " + Settings.player.MaxPower)));
                return;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(target.getName() + "'s power set to " + (double) amount)));
            target.setPower(amount);
            return;
        }
        if (action.equalsIgnoreCase("add")) {
            if (amount <= -1) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must supply a value between 0 and " + (Settings.player.MaxPower - target.getPower()))));
                return;
            }
            double newPower = target.getPower();
            newPower += amount;
            if (newPower > Settings.player.MaxPower) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Supplied number is greater than max power of " + Settings.player.MaxPower)));
                return;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(target.getName() + "'s power set to " + newPower)));
            target.setPower(newPower);
            return;
        }
        if (action.equalsIgnoreCase("remove")) {
            if (amount <= -1) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must supply a value between 0 and " + Settings.player.MaxPower)));
                return;
            }
            double newPower = target.getPower();
            newPower -= amount;
            if (newPower < 0) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Supplied number is less than 0")));
                return;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(target.getName() + "'s power set to " + newPower)));
            target.setPower(newPower);
            return;
        }
        if (action.equalsIgnoreCase("reset")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Reset the power of " + target.getName())));
            target.setPower(0D);
            return;
        }
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Invalid action: " + action)));
    }
}
