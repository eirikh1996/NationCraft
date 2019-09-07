package io.github.eirikh1996.nationcraft.commands;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.commands.subcommands.settlement.*;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class SettlementCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        SettlementSubCommand subCommand = null;
        if (!command.getName().equalsIgnoreCase("settlement")){
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + Messages.MUST_BE_PLAYER);
            return true;
        }
        if (!commandSender.hasPermission("nationcraft.settlement")){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + Messages.NO_PERMISSION);
            return true;
        }
        if (strings.length == 0){
            return true;
        }
        else if (strings[0].equalsIgnoreCase("create")){
            if (!commandSender.hasPermission("nationcraft.settlement.create")){
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + Messages.NO_PERMISSION);
                return true;
            } else if (strings.length < 2){
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a name");
                return true;
            }
            subCommand = new CreateSettlementSubCommand((Player) commandSender, strings[1]);
        }
        else if (strings[0].equalsIgnoreCase("claim")){
            Shape shape;
            int radius;
            String settlementName;
            try {
                shape = Shape.getShape(strings[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                shape = Shape.SINGLE;
            }
            try {
                radius = Integer.parseInt(strings[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                radius = 0;
            } catch (NumberFormatException e){
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + strings[1] + " is not a number");
                return true;
            }
            try {
                settlementName = strings[2];
            } catch (ArrayIndexOutOfBoundsException e){
                settlementName = "";
            }
            subCommand = new ClaimSettlementSubCommand((Player) commandSender, shape, radius, settlementName);
        } else if (strings[0].equalsIgnoreCase("unclaim")){
            Shape shape;
            int radius;
            String settlementName;
            try {
                shape = Shape.getShape(strings[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                shape = Shape.SINGLE;
            }
            try {
                radius = Integer.parseInt(strings[1]);
            } catch (ArrayIndexOutOfBoundsException e){
                radius = 0;
            } catch (NumberFormatException e){
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + strings[1] + " is not a number");
                return true;
            }
            try {
                settlementName = strings[2];
            } catch (ArrayIndexOutOfBoundsException e){
                settlementName = "";
            }
            subCommand = new UnclaimSettlementSubCommand((Player) commandSender, shape, radius, settlementName);
        } else if (strings[0].equalsIgnoreCase("nearest")){
            subCommand = new NearestSettlementSubCommand((Player) commandSender);
        }
        if (subCommand == null){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + strings[0] + " is not a valid subcommand");
            return true;
        }
        subCommand.execute();
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0){
            return Collections.emptyList();
        }
        ArrayList<String> subCmds = new ArrayList<>();
        if (strings.length == 1) {
            if (commandSender.hasPermission("nationcraft.settlement.create")) {
                subCmds.add("create");
            }
            if (commandSender.hasPermission("nationcraft.settlement.claim")) {
                subCmds.add("claim");
            }
            if (commandSender.hasPermission("nationcraft.settlement.nearest")) {
                subCmds.add("nearest");
            }
            if (commandSender.hasPermission("nationcraft.settlement.info")) {
                subCmds.add("info");
            }
            if (commandSender.hasPermission("nationcraft.settlement.siege")) {
                subCmds.add("siege");
            }
            if (commandSender.hasPermission("nationcraft.settlement.unclaim")) {
                subCmds.add("unclaim");
            }
        } else if (strings[0].equalsIgnoreCase("claim")){
            for (Shape sh : Shape.values()){
                if (sh.equals(Shape.ALL)){
                    continue;
                }
                subCmds.add(sh.name().toLowerCase());
            }
            if (strings.length == 4 && commandSender.hasPermission("nationcraft.settlement.claim.other")){
                for (Settlement set : SettlementManager.getInstance().getAllSettlements()){
                    if (set == null){
                        continue;
                    }
                    subCmds.add(set.getName());
                }
            }
        }
        ArrayList<String> completions = new ArrayList<>();
        Collections.sort(subCmds);
        for (String completion : subCmds) {
            if (completion.startsWith(strings[strings.length - 1].toLowerCase())) {
                completions.add(completion);
            }
        }
        return completions;
    }
}
