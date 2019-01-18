package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        ChatMode mode = PlayerManager.getInstance().getChatModeByPlayer(event.getPlayer());
        Nation nation = NationManager.getInstance().getNationByPlayer(event.getPlayer());
        for (Player p : event.getRecipients()) {
            if (mode == ChatMode.SETTLEMENT) {

            } else if (mode == ChatMode.NATION) {
                if (!nation.getPlayers().containsKey(p.getUniqueId())){
                    event.getRecipients().remove(p);
                }
            } else if (mode == ChatMode.ALLY){

            }
        }
    }
}
