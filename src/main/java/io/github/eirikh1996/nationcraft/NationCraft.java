package io.github.eirikh1996.nationcraft;

import io.github.eirikh1996.nationcraft.commands.NationCommand;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.listener.BlockListener;
import io.github.eirikh1996.nationcraft.listener.ChatListener;
import io.github.eirikh1996.nationcraft.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.nation.NationFileManager;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NationCraft extends JavaPlugin {
	private static NationCraft instance;

	
	public void onEnable() {

		PlayerManager.initialize();
		NationManager.initialize();
		if (NationManager.getInstance().getNations().isEmpty()){
			getLogger().info("No nation files loaded");
		}
		else {
			getLogger().info(String.format("Loaded %d nation files", NationManager.getInstance().getNations().size()));
		}
		//Load config file
		this.saveDefaultConfig();

		//Read config file
		Settings.maxPlayersPerNation = getConfig().getInt("MaxPlayersPerNation", 50);

		//Load players file

		//register events
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new NationFileManager(), this);

		//Now register commands
		this.getCommand("nation").setExecutor(new NationCommand());
		this.getCommand("nation").setTabCompleter(new NationCommand());
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
