package io.github.eirikh1996.nationcraft.bukkit.utils;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceHolderUtils extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "nationcraft";
    }

    @Override
    public String getAuthor() {
        return String.join(", ", NationCraft.getInstance().getAuthors());
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
        final NCPlayer player = PlayerManager.getInstance().getPlayer(p.getUniqueId());
        if (params.equalsIgnoreCase("nation")) {
            final Nation pn = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
            return pn != null ? pn.getName(p.getUniqueId()) : "Wilderness";
        }

        if (params.equalsIgnoreCase("settlement")) {
            final Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(p.getUniqueId());
            return ps != null ? ps.getName() : "No settlement";
        }

        if (params.equalsIgnoreCase("power")) {
            return String.valueOf(player.getPower());
        }

        if (params.equalsIgnoreCase("maxpower")) {
            return String.valueOf(Settings.player.MaxPower);
        }

        if (params.equalsIgnoreCase("nationterr")) {
            return String.valueOf(player.getNation().getTerritoryManager().size());
        }

        return null;
    }
}
