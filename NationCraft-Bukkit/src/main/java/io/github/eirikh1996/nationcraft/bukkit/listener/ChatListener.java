package io.github.eirikh1996.nationcraft.bukkit.listener;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.events.player.PlayerChatEvent;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        final NCPlayer ncPlayer = PlayerManager.getInstance().getPlayer(event.getPlayer().getUniqueId());
        PlayerChatEvent chatEvent = new PlayerChatEvent(ncPlayer, event.getFormat(), event.getMessage(), new HashSet<>());
        NationCraftAPI.getInstance().callEvent(chatEvent);
        event.setCancelled(chatEvent.isCancelled());

        /*
        ChatMode mode = ncPlayer.getChatMode();
        Nation nation = NationManager.getInstance().getNationByPlayer(event.getPlayer().getUniqueId());
        for (Player p : event.getRecipients()) {
            if (mode == ChatMode.SETTLEMENT) {
                Settlement settlement = SettlementManager.getInstance().getSettlementByPlayer(p.getUniqueId());
                if (!settlement.getPlayers().containsKey(p.getUniqueId())){
                    event.getRecipients().remove(p);
                }
            } else if (mode == ChatMode.NATION) {
                if (!nation.getPlayers().containsKey(PlayerManager.getInstance().getPlayer(p.getUniqueId()))){
                    event.getRecipients().remove(p);
                }
            } else if (mode == ChatMode.ALLY){
                Nation ally = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
                if (!nation.getAllies().contains(ally)){
                    event.getRecipients().remove(p);
                }

            }
        }*/
    }
}
