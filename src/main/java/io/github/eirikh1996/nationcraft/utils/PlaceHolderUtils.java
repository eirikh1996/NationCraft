package io.github.eirikh1996.nationcraft.utils;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceHolderUtils extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "nationcraft";
    }

    @Override
    public String getAuthor() {
        return "eirikh1996";
    }

    @Override
    public String getVersion() {
        return NationCraft.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        if (p == null) {
            return "";
        }
        if (params.equals("nation")) {
            final Nation pn = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
            return pn != null ? pn.getName(p.getUniqueId()) : "Wilderness";
        }

        if (params.equals("settlement")) {
            final Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(p.getUniqueId());
            return ps != null ? ps.getName() : "No settlement";
        }
        return null;
    }
}
