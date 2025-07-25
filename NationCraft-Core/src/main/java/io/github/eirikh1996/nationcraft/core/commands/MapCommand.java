package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.map.MapHeightSubCommand;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.map.MapHelpSubCommand;
import io.github.eirikh1996.nationcraft.core.messages.Messages;

import java.util.Arrays;

public class MapCommand extends Command {

    MapCommand() {
        super("map");
        addChild(new MapHeightSubCommand());
        addChild(new MapHelpSubCommand());
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        long startTime = System.currentTimeMillis();
        if (!(sender instanceof NCPlayer p)){
            sender.sendMessage(Messages.ERROR + Messages.MUST_BE_PLAYER);
            return;
        }
        if (args.length == 0) {
            Messages.generateTerritoryMap(p);
            return;
        }
        if (!children.containsKey(args[0])) {
            return;
        }
        children.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
            /*
            @NotNull String status = "";;
            final NCPlayer player = PlayerManager.getInstance().getPlayer(p.getUniqueId());
            if (!player.isAutoUpdateTerritoryMap()){
                player.setAutoUpdateTerritoryMap(true);
                status = "enabled";
            } else {
                player.setAutoUpdateTerritoryMap(false);
                status = "disabled";
            }
            p.sendMessage("Automatic map updates " + status);
        } else if (strings[0].equalsIgnoreCase("help") || strings[0].equalsIgnoreCase("?")) {
            p.sendMessage("=================== Map help ===================");
            p.sendMessage(ChatColor.RED + "Enemy");
            p.sendMessage(ChatColor.WHITE + "Neutral");
            p.sendMessage(ChatColor.DARK_PURPLE + "Ally");
            p.sendMessage(ChatColor.GOLD + "Safezone");
            p.sendMessage(ChatColor.DARK_RED + "Warzone");
        } else if (strings[0].equalsIgnoreCase("height")) {
            if (strings.length < 2) {
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a height value");
            }
            int height;
            try {
                height = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + strings[1] + " is not a number!");
                return true;
            }
            subCommand = new HeightSubCommand(commandSender, height);
        }
        long endTime = System.currentTimeMillis();
        if (subCommand != null) {
            subCommand.execute();
        }
        if (Settings.Debug){
            Bukkit.broadcastMessage("Command processing took (ms): " + (endTime - startTime));
        }
        return true;*/
    }
}
