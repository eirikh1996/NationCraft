package io.github.eirikh1996.nationcraft.bukkit.utils;

import io.github.eirikh1996.nationcraft.api.objects.NCBlock;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.objects.NCBukkitBlockSender;
import io.github.eirikh1996.nationcraft.bukkit.objects.NCBukkitConsole;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitUtils {
    private static BukkitUtils instance;

    private BukkitUtils() {}

    public NCLocation bukkitToNCLoc(Location bLoc) {
        return new NCLocation(bLoc.getWorld().getName(), bLoc.getX(), bLoc.getY(), bLoc.getZ(), bLoc.getPitch(), bLoc.getYaw());
    }

    public NCCommandSender getCorrespondingSender(CommandSender sender) {
        if (sender instanceof Player) {
            return PlayerManager.getInstance().getPlayer(((Player) sender).getUniqueId());
        } else if (sender instanceof ConsoleCommandSender) {
            return new NCBukkitConsole((ConsoleCommandSender) sender);
        } else {
            return new NCBukkitBlockSender((BlockCommandSender) sender);
        }
    }

    public NCBlock getNCBlock(Block block) {
        return new NCBlock(block.getType().name(), bukkitToNCLoc(block.getLocation()));
    }

    public static BukkitUtils getInstance() {
        if (instance == null) {
            instance = new BukkitUtils();
        }
        return instance;
    }
}
