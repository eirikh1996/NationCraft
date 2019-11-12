package io.github.eirikh1996.nationcraft.settlement.siege;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class SiegeManager extends BukkitRunnable {
    private static SiegeManager instance;
    private final List<Siege> sieges = new ArrayList<>();
    private final Queue<SiegeTask> completedTaskQueue = new LinkedList<>();

    @Override
    public void run() {
        processQueue();
        processSieges();
    }

    public void addSiege(Siege siege){
        sieges.add(siege);
    }

    public static synchronized SiegeManager getInstance(){
        if (instance == null)
            instance = new SiegeManager();
        return instance;
    }

    void submitCompletedTask(SiegeTask task){
        completedTaskQueue.add(task);
    }

    private void processSieges() {
        final List<Siege> completedSieges = new ArrayList<>();
        if (sieges.isEmpty()){
            return;
        }

        for (Siege siege : sieges){
            new SiegePlayerCheckTask(siege).runTask(NationCraft.getInstance());
            if (siege.getAttackerPresenceTime() >= Settings.SiegeRequiredAttackerPresenceTime){
                siege.getSettlement().setNation(siege.getAttacker().getName());
                completedSieges.add(siege);
            } else if (siege.getAttackerAbsenceTime() >= Settings.SiegeMaximumAttackerAbsenceTime){
                Bukkit.broadcastMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s's siege on %s was sucessfully crushed by %s", siege.getAttacker().getName(), siege.getSettlement().getName(), siege.getDefender().getName()));
                completedSieges.add(siege);
            }
        }
        if (completedSieges.size() > 0){
            sieges.removeAll(completedSieges);
        }
    }

    private void processQueue(){
        if (completedTaskQueue.isEmpty()){
            return;
        }
        SiegeTask poll = completedTaskQueue.poll();
        if (poll instanceof SiegePlayerCheckTask){
            SiegePlayerCheckTask task = (SiegePlayerCheckTask) poll;
            if (task.isEnemyPlayersInTownCenter()){
                task.getSiege().setAttackerAbsenceTime(0);
                int time = task.getSiege().getAttackerPresenceTime();
                time++;
                task.getSiege().setAttackerPresenceTime(time);
            } else {
                task.getSiege().setAttackerPresenceTime(0);
                int time = task.getSiege().getAttackerAbsenceTime();
                time++;
                task.getSiege().setAttackerAbsenceTime(time);
            }
        }
    }
}
