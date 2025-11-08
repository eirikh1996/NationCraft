package io.github.eirikh1996.nationcraft.bukkit.hooks.chat;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.player.BukkitPlayerManager;
import mineverse.Aust1n46.chat.MineverseChat;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.channel.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;

public class VentureChatHook implements Listener {

    public VentureChatHook() {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;
        final MineverseChatPlayer mcp = MineverseChatAPI.getOnlineMineverseChatPlayer(event.getPlayer());
        ChatChannel current = mcp.getCurrentChannel();
        ChatChannel quick = mcp.getQuickChannel();
        if (isNationChannel(current) ||isNationChannel(quick)) {
            NCPlayer sender = BukkitPlayerManager.getInstance().getPlayer(event.getPlayer());
            Iterator<Player> iter = event.getRecipients().iterator();
            while (iter.hasNext()) {
                final NCPlayer recipient = BukkitPlayerManager.getInstance().getPlayer(iter.next());
                if (!sender.hasNation() && recipient.hasNation()) {
                    iter.remove();
                } else if (!sender.isInNationWith(recipient)) {
                    iter.remove();
                }
            }
        } else if (isSettlementChannel(current) || isSettlementChannel(quick)) {
            NCPlayer sender = BukkitPlayerManager.getInstance().getPlayer(event.getPlayer());
            Iterator<Player> iter = event.getRecipients().iterator();
            while (iter.hasNext()) {
                final NCPlayer recipient = BukkitPlayerManager.getInstance().getPlayer(iter.next());
                if (!sender.hasSettlement() && recipient.hasSettlement()) {
                    iter.remove();
                } else if (!sender.isInSettlementWith(recipient)) {
                    iter.remove();
                }
            }
        } else if (isAllyChannel(current) || isAllyChannel(quick)) {
            NCPlayer sender = BukkitPlayerManager.getInstance().getPlayer(event.getPlayer());
            Iterator<Player> iter = event.getRecipients().iterator();
            while (iter.hasNext()) {
                final NCPlayer recipient = BukkitPlayerManager.getInstance().getPlayer(iter.next());
                if (!sender.hasNation() && recipient.hasNation()) {
                    iter.remove();
                } else if (!sender.isAlliedWith(recipient)) {
                    iter.remove();
                }
            }
        } else if (isTruceChannel(current) || isTruceChannel(quick)) {
            NCPlayer sender = BukkitPlayerManager.getInstance().getPlayer(event.getPlayer());
            Iterator<Player> iter = event.getRecipients().iterator();
            while (iter.hasNext()) {
                final NCPlayer recipient = BukkitPlayerManager.getInstance().getPlayer(iter.next());
                if (!sender.hasNation() && recipient.hasNation()) {
                    iter.remove();
                } else if (!sender.isAlliedWith(recipient)) {
                    iter.remove();
                }
            }
        }
    }

    private boolean isNationChannel(ChatChannel channel) {
        return channel.getName().equalsIgnoreCase("Nation");
    }

    private boolean isSettlementChannel(ChatChannel channel) {
        return channel.getName().equalsIgnoreCase("Settlement");
    }

    private boolean isAllyChannel(ChatChannel channel) {
        return channel.getName().equalsIgnoreCase("Ally");
    }

    private boolean isTruceChannel(ChatChannel channel) {
        return channel.getName().equalsIgnoreCase("Truce");
    }
}
