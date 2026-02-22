package io.github.eirikh1996.nationcraft.core.settlement.siege;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class SiegeManager implements Runnable {
    private static SiegeManager instance;
    private final List<Siege> sieges = new ArrayList<>();
    private final Queue<SiegeTask> completedTaskQueue = new LinkedList<>();
    private NationCraftMain main;

    @Override
    public void run() {
        processQueue();
        processSieges();
    }

    public void addSiege(Siege siege){
        sieges.add(siege);
    }

    public void initialize(NationCraftMain main) {
        this.main = main;
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
            new Thread(new SiegePlayerCheckTask(siege)).start();
            if (siege.getAttackerPresenceTime() > -1L &&
                    ((System.currentTimeMillis() - siege.getAttackerPresenceTime()) / 1000) >= Settings.SiegeRequiredAttackerPresenceTime){
                main.broadcast(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(String.format("%s has sucessfully taken control of %s from %s", siege.getAttacker().getName(), siege.getSettlement().getName(), siege.getDefender().getName()))));
                siege.getSettlement().setNation(siege.getAttacker());
                completedSieges.add(siege);
            } else if (siege.getAttackerAbsenceTime() > -1L &&
                    ((System.currentTimeMillis() - siege.getAttackerAbsenceTime()) / 1000) >= Settings.SiegeMaximumAttackerAbsenceTime){
                main.broadcast(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(String.format("%s's siege on %s was sucessfully crushed by %s", siege.getAttacker().getName(), siege.getSettlement().getName(), siege.getDefender().getName()))));
                completedSieges.add(siege);
            }
        }
        if (!completedSieges.isEmpty()){
            sieges.removeAll(completedSieges);
        }
    }

    private void processQueue(){
        if (completedTaskQueue.isEmpty()){
            return;
        }
        SiegeTask poll = completedTaskQueue.poll();
        if (poll instanceof SiegePlayerCheckTask task){
            if (task.isEnemyPlayersInTownCenter()){
                task.getSiege().setAttackerAbsenceTime(-1L);
                if (task.getSiege().getAttackerPresenceTime() <= -1L) {
                    task.getSiege().setAttackerPresenceTime(System.currentTimeMillis());
                }
            } else {
                task.getSiege().setAttackerPresenceTime(-1L);
                if (task.getSiege().getAttackerAbsenceTime() <= -1L) {
                    task.getSiege().setAttackerAbsenceTime(System.currentTimeMillis());
                }
            }
        }
    }
}
