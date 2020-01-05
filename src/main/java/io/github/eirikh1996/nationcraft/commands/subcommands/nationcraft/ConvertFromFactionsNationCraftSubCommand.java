package io.github.eirikh1996.nationcraft.commands.subcommands.nationcraft;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.messages.Messages.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class ConvertFromFactionsNationCraftSubCommand extends NationCraftSubCommand {
    public ConvertFromFactionsNationCraftSubCommand(Player sender) {
        super(sender);
    }

    @Override
    public void execute() {
        File mstore = new File(Bukkit.getWorldContainer(),"mstore");
        if (!mstore.exists()){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Factions has never been run on the server");
            return;
        }
        final Gson gson = new Gson();
        final File boardFile = new File(mstore, "factions_board");
        final Map<String, Collection<Territory>> territoryListMap = new HashMap<>();
        final Map<String, NCPlayer> playerMap = new HashMap<>();
        /*
        Step 1: Get the territories
         */
        for (File file : boardFile.listFiles()) {
            String worldName = file.getName().replace(".json", "");
            Map<String, String> territories;
            try {
                territories = gson.fromJson(new FileReader(boardFile), Map.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            for (String coords : territories.keySet()) {
                String nationID = territories.get(coords);
                String[] parts = coords.split(",");
                Territory terr = new Territory(Bukkit.getWorld(worldName), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                if (territoryListMap.containsKey(nationID)) {
                    territoryListMap.get(nationID).add(terr);

                } else {
                    Collection<Territory> territory = new HashSet<>();
                    territory.add(terr);
                    territoryListMap.put(nationID, territory);
                }
            }
        }
        /*
        Step two: Get player data
         */
        final File playersDir = new File(mstore, "factions_mplayer");
        for (File playerData : playersDir.listFiles()){
            final Map pData;
            try {
                pData = gson.fromJson(new FileReader(playerData), Map.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            final UUID playerID = UUID.fromString(playerData.getName().replace(".json", ""));
            NCPlayer player = new NCPlayer(playerID, Bukkit.getOfflinePlayer(playerID).getName());
            player.setPower((double) pData.get("power"));
            playerMap.put((String) pData.get("factionId"), player);
        }
        final File factionsDir = new File(mstore, "factions_faction");
        for (File factionData : factionsDir.listFiles()){
            if (factionData.getName().contains("none")){
                continue;
            }
            final Map faction;
            try {
                faction = gson.fromJson(new FileReader(factionData), Map.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            Map<NCPlayer, Ranks> players = new HashMap<>();
            String name = (String) faction.get("name");
            long createdAtMillis = (long) faction.get("createdAtMillis");


        }
    }
}
