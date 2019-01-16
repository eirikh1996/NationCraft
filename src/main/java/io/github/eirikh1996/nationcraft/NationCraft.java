package io.github.eirikh1996.nationcraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import io.github.eirikh1996.nationcraft.commands.NationCommand;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.NationSaveTask;
import org.bukkit.plugin.java.JavaPlugin;

public class NationCraft extends JavaPlugin {
	private static NationCraft instance;

	
	public void onEnable() {

		//Load config file
		this.saveDefaultConfig();
		NationManager.initialize();
		//Read config file
		Settings.maxPlayersPerNation = getConfig().getInt("MaxPlayersPerNation", 50);

		//Load players file
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
		//

		//Now register commands
		this.getCommand("nation").setExecutor(new NationCommand());
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onLoad() {
		instance = this;
	}
	
	public static synchronized NationCraft getInstance() {
		return instance;
	}
}
