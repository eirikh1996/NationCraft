package io.github.eirikh1996.nationcraft.nation;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerInviteEvent;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NationFileManager implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNationCreate(NationCreateEvent event){
        NationCraft.getInstance().getLogger().info(event.getNation().getName());
        NationManager.getInstance().saveNationToFile(event.getNation());
    }

    @EventHandler
    public void onPlayerJoinNation(NationPlayerJoinEvent event){
        NationManager.getInstance().saveNationToFile(event.getNation());
    }

    @EventHandler
    public void onPlayerInvite(NationPlayerInviteEvent event){
        NationManager.getInstance().saveNationToFile(event.getNation());
    }
}
