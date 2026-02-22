package io.github.eirikh1996.nationcraft.core.settlement.siege;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;

public class Siege {
    private final Settlement settlement;
    private final Nation defender;
    private final Nation attacker;
    private long attackerPresenceTime = -1L;
    private long attackerAbsenceTime = -1L;

    public Siege(Settlement settlement, Nation attacker) {
        this.settlement = settlement;
        defender = NationManager.getInstance().getNationBySettlement(settlement);

        this.attacker = attacker;
    }

    public Nation getAttacker() {
        return attacker;
    }

    public Nation getDefender() {
        return defender;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public long getAttackerAbsenceTime() {
        return attackerAbsenceTime;
    }

    public void setAttackerAbsenceTime(long attackerAbsenceTime) {
        this.attackerAbsenceTime = attackerAbsenceTime;
    }

    public long getAttackerPresenceTime() {
        return attackerPresenceTime;
    }

    public void setAttackerPresenceTime(long attackerPresenceTime) {
        this.attackerPresenceTime = attackerPresenceTime;
    }
}
