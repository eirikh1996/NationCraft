package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;

public class CreateNationSubCommand extends NationSubCommand {
    private final String name;
    public CreateNationSubCommand(Player sender, String name){
        super(sender);
        this.name = name;
    }
    @Override
    public void execute() {
        if (!sender.hasPermission("nationcraft.nation.create")) {
            sender.sendMessage("Error: You do not have permission to use this command!");
            return;
        }
        if (name.length() <= 2) {
            sender.sendMessage("Error: Nation names must be at least 2 characters long");
            return;
        }
        for (Nation existing : NationManager.getInstance().getNations()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                sender.sendMessage(String.format("Error: A nation with the name of %s already exists!", name));
                return;
            }
        }
        if (NationManager.getInstance().getNationByPlayer(sender) != null) {
            sender.sendMessage(Messages.ERROR + "You must leave your current nation before you can create one!");
            return;
        }
        String description = "Default description";
        Set<Chunk> territory = new HashSet<>();
        String capital = "(none)";
        List<String> settlements = new ArrayList<>();
        List<String> allies = new ArrayList<>();
        List<String> enemies = new ArrayList<>();
        Map<UUID, Ranks> players = new HashMap<>();
        players.put(((Player) sender).getUniqueId(), Ranks.LEADER);

        Nation newNation = new Nation(name, description, capital, allies, enemies, settlements, territory, players);
        //Call event
        NationCreateEvent event = new NationCreateEvent(newNation, sender);
        NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            sender.sendMessage("Cancelled: " + event.isCancelled());
            return;
        }
        sender.sendMessage("You successfully created a new nation named " + ChatColor.GREEN + name);
        NationManager.getInstance().getNations().add(newNation);
    }
}
