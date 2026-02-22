package io.github.eirikh1996.nationcraft.bukkit.listener;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.events.player.PlayerChatEvent;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event){
        if (Settings.UseExternalChatPlugin) {
            return;
        }
        final NCPlayer ncPlayer = PlayerManager.getInstance().getPlayer(event.getPlayer().getUniqueId());
        final ChatMode mode = ncPlayer.getChatMode();
        String format = "";
        switch (mode) {
            case NATION -> format = Settings.chat.nationFormat;
            case SETTLEMENT -> format = Settings.chat.settlementFormat;
            case TRUCE -> format = Settings.chat.truceFormat;
            case ALLY -> format = Settings.chat.allyFormat;
            case GLOBAL -> format = Settings.chat.globalFormat;
        }
        final MiniMessage mm = MiniMessage.miniMessage();


        String dFormat = format;
        event.renderer(((source, sourceDisplayName, message, viewer) -> {
            TextComponent nationName;
            String finalFormat = dFormat;
            if (viewer instanceof Player p) {
                nationName = ncPlayer.hasNation() ? ncPlayer.getNation().getName(p.getUniqueId()) : Component.text("-");
            } else if (viewer instanceof ConsoleCommandSender) {
                if (mode == ChatMode.NATION) {
                    finalFormat = "%NATION% nation chat";
                } else if (mode == ChatMode.SETTLEMENT) {
                    finalFormat = "%SETTLEMENT% settlement chat";
                } else if (mode == ChatMode.ALLY){
                    finalFormat = "Ally chat " + dFormat;
                } else if (mode == ChatMode.TRUCE) {
                    finalFormat = "Truce chat " + dFormat;
                }

                nationName = Component.text(ncPlayer.hasNation() ? ncPlayer.getNation().getName() : "-");
            } else {
                nationName = Component.text(ncPlayer.getNation().getName());
            }
            String mmString = finalFormat.replace("%NATION%", mm.serialize(nationName)).replace("%SETTLEMENT%", ncPlayer.getSettlement() == null ? "-" : ncPlayer.getSettlement().getName());
            Component formatComponent = mm.deserialize(mmString);
            return formatComponent
                    .append(Component.space())
                    .append(sourceDisplayName)
                    .append(Component.space())
                    .append(message);
        }));
       /*
        PlayerChatEvent chatEvent = new PlayerChatEvent(ncPlayer, event.getFormat(), event.getMessage(), recipients);
        NationCraftAPI.getInstance().callEvent(chatEvent);
        event.setCancelled(chatEvent.isCancelled());
        event.setFormat(chatEvent.getFormat());
        event.setMessage(event.getMessage());
        final Iterator<Player> iter = event.getRecipients().iterator();
        while (iter.hasNext()) {
            Player recipient = iter.next();
            final NCPlayer ncRecipient = PlayerManager.getInstance().getPlayer(recipient.getUniqueId());
            if (chatEvent.getRecipients().contains(ncRecipient)) {
                continue;
            }
            iter.remove();
        }*/

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
