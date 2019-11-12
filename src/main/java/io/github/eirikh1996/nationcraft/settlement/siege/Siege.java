package io.github.eirikh1996.nationcraft.settlement.siege;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;

public class Siege {
    private final Settlement settlement;
    private final Nation defender;
    private final Nation attacker;
    private int attackerPresenceTime = 0;
    private int attackerAbsenceTime = 0;

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

    public int getAttackerAbsenceTime() {
        return attackerAbsenceTime;
    }

    public void setAttackerAbsenceTime(int attackerAbsenceTime) {
        this.attackerAbsenceTime = attackerAbsenceTime;
    }

    public int getAttackerPresenceTime() {
        return attackerPresenceTime;
    }

    public void setAttackerPresenceTime(int attackerPresenceTime) {
        this.attackerPresenceTime = attackerPresenceTime;
    }
}
