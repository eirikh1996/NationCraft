package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.Chunk;

public class SettlementManager {
	private static List<Settlement> settlements;
	private static List<Nation> nations;
	
	public List<Settlement> getSettlements(){
		return settlements;
	}
	
	public boolean isOwnNationTerritory(Chunk territory) {

		return false;
	}
	public void saveToFile(Settlement s) {
		File settlementFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + s.getName() + ".settlement");
		if (!settlementFile.exists()) {
			try {
				settlementFile.createNewFile();
				PrintWriter writer = new PrintWriter(settlementFile);
				writer.println("name: " + s.getName());
				writer.println("townCenter: " + s.getTownCenter());
				writer.println("territory:");
				for (int i = 0 ; i <= s.getTerritory().size() ; i++) {
					writer.println("- " + s.getTerritory().get(i));
				}
				writer.println("players:");
				for (int i = 0 ; i <= s.getPlayers().size() ; i++) {
					writer.println("- " + s.getPlayers().get(i));
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

		}
	}
}
