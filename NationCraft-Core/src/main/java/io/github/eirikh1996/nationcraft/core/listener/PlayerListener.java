package io.github.eirikh1996.nationcraft.core.listener;

import io.github.eirikh1996.nationcraft.api.events.EventListener;
import io.github.eirikh1996.nationcraft.api.events.player.PlayerMoveEvent;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import org.jetbrains.annotations.Nullable;

public class PlayerListener {
    
    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        NCPlayer player = event.getPlayer();
        NCLocation from = event.getOrigin();
        NCLocation to = event.getDestination();

        @Nullable Nation fromN = NationManager.getInstance().getNationAt(from);
        @Nullable Nation toN = NationManager.getInstance().getNationAt(to);
        if (player.isAutoUpdateTerritoryMap()) {

            if (from.getTerritory() != to.getTerritory()) {
                Messages.generateTerritoryMap(player);
            }
        }
        Settlement fromS = SettlementManager.getInstance().getSettlementAt(from);
        Settlement toS = SettlementManager.getInstance().getSettlementAt(to);
        if (toS != null || fromS != null){
            if (toS == null){
                event.getPlayer().sendActionBar("Leaving the settlement of " + fromS.getName());
            } else if (fromS == null){
                event.getPlayer().sendActionBar("Entering the settlement of " + toS.getName());
            } else if (toS.getTownCenter().equalsTerritory(to.getTerritory()) && !toS.getTownCenter().equalsTerritory(from.getTerritory())){
                event.getPlayer().sendActionBar("Entering the town center of " + toS.getName());
            } else if (fromS.getTownCenter().equalsTerritory(from.getTerritory()) && !fromS.getTownCenter().equalsTerritory(to.getTerritory())){
                event.getPlayer().sendActionBar("Leaving the town center of " + fromS.getName());
            }
        }
        if (fromN != toN){
            String fromNationName = (fromN != null ? NationManager.getInstance().getColor(player, fromN) + fromN.getName() : TextColor.DARK_GREEN + "Wilderness") + TextColor.RESET;
            String toNationName = (toN != null ? NationManager.getInstance().getColor(player, toN) + toN.getName() : TextColor.DARK_GREEN + "Wilderness") + TextColor.RESET;
            event.getPlayer().sendTitle(toNationName, toN == null ? "" : toN.getDescription(),10,70,20);
            event.getPlayer().sendMessage(String.format("Leaving %s, entering %s", fromNationName, toNationName));
        }

        //auto update map if player is moving
    }


}
