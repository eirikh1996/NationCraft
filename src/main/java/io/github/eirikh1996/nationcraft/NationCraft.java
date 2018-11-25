package io.github.eirikh1996.nationcraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import io.github.eirikh1996.nationcraft.commands.ClaimSettlementTerritoryCommand;
import io.github.eirikh1996.nationcraft.commands.CreateNationCommand;
import io.github.eirikh1996.nationcraft.commands.CreateSettlementCommand;
import io.github.eirikh1996.nationcraft.commands.NationInfoCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class NationCraft extends JavaPlugin {
	private static NationCraft instance;
	
	public void onEnable() {
		File playerFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "players.yml");
		if (!playerFile.exists()) {
			try {
				PrintWriter writer = new PrintWriter("players.yml");
				writer.println("players:");
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Now register commands
		this.getCommand("claimsettlementterritory").setExecutor(new ClaimSettlementTerritoryCommand(this));
		this.getCommand("createnation").setExecutor(new CreateNationCommand(this));
		this.getCommand("createsettlement").setExecutor(new CreateSettlementCommand());
		this.getCommand("nationinfo").setExecutor(new NationInfoCommand(this));
	}
	
	public void onDisable() {
		
	}
	
	public void onLoad() {
		instance = this;
	}
	
	public static synchronized NationCraft getInstance() {
		return instance;
	}
}
