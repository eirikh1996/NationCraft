package io.github.eirikh1996.nationcraft.api.settlement.siege;

public abstract class SiegeTask implements Runnable {
    protected final Siege siege;

    protected SiegeTask(Siege siege) {
        this.siege = siege;
    }

    @Override
    public void run() {
        try {
            execute();
            SiegeManager.getInstance().submitCompletedTask(this);
        } catch (Throwable t){
            //NationCraft.getInstance().getLogger().severe("Something went wrong while processing a siege on settlement " + siege.getSettlement() + " by " + siege.getAttacker().getName());
            throw new SiegeException(null, t);
        }

    }
    public abstract void execute();

    public Siege getSiege() {
        return siege;
    }
}
