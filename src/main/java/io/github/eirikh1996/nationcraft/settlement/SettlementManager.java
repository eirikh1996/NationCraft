package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public class SettlementManager implements Iterable<Settlement> {
	private static List<Settlement> settlements;
	private static SettlementManager instance;
	public SettlementManager(){

	}
	public static void initialize(){
		instance = new SettlementManager();
	}
	public List<Settlement> getSettlements(){
		return settlements;
	}

	public static SettlementManager getInstance(){
		return instance;
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
				for (Chunk tChunk : s.getTerritory()) {
					writer.println("- [" + tChunk.getWorld().getName() + "," + tChunk.getX() + "," + tChunk.getZ() + "]");
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


	@NotNull
	@Override
	public Iterator<Settlement> iterator() {
		return settlements.iterator();
	}
}
