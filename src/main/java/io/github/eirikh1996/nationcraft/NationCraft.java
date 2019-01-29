package io.github.eirikh1996.nationcraft;

import io.github.eirikh1996.nationcraft.commands.MapCommand;
import io.github.eirikh1996.nationcraft.commands.NationCommand;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.listener.BlockListener;
import io.github.eirikh1996.nationcraft.listener.ChatListener;
import io.github.eirikh1996.nationcraft.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationFileManager;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class NationCraft extends JavaPlugin {
	private static NationCraft instance;

	
	public void onEnable() {

		PlayerManager.initialize();
		NationManager.initialize();
		NationManager nManager = NationManager.getInstance();
		if (nManager.getNations().isEmpty()){
			getLogger().info("No nation files loaded");
		}
		else {
			getLogger().info(String.format("Loaded %d nation files", NationManager.getInstance().getNations().size()));
		}
		boolean szCreated = false;
		boolean wzCreated = false;
		File szFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/Safezone.nation");
		File wzFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/Warzone.nation");
		if (!szFile.exists()){
			szCreated = nManager.createSafezone();
			if (szCreated)
				getLogger().info("Safezone file created.");
			else
				getLogger().warning("Safezone failed to create file!");
		}
		if (!wzFile.exists()){
			wzCreated = nManager.createWarzone();
			if (wzCreated)
			getLogger().info("Safezone file created.");
			else
			getLogger().warning("Safezone failed to create file!");
		}
		if (wzCreated || szCreated){
			nManager.reload();
		}

		//Load config file
		this.saveDefaultConfig();

		//Read config file
		Settings.maxPlayersPerNation = getConfig().getInt("MaxPlayersPerNation", 50);
		List<String> forbiddenCommandsInEnemyTerritory = getConfig().getStringList("forbiddenCommandsInEnemyTerritory");
		if (forbiddenCommandsInEnemyTerritory == null){
			Settings.forbiddenCommandsInEnemyTerritory = Collections.emptyList();
		} else {
			Settings.forbiddenCommandsInEnemyTerritory = forbiddenCommandsInEnemyTerritory;
		}

		//Load players file

		//register events
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new NationFileManager(), this);

		//Now register commands
		this.getCommand("nation").setExecutor(new NationCommand());
		this.getCommand("map").setExecutor(new MapCommand());
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
