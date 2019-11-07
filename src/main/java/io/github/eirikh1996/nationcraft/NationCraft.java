package io.github.eirikh1996.nationcraft;

import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.commands.*;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.listener.BlockListener;
import io.github.eirikh1996.nationcraft.listener.ChatListener;
import io.github.eirikh1996.nationcraft.listener.EntityListener;
import io.github.eirikh1996.nationcraft.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class NationCraft extends JavaPlugin {
	private static NationCraft instance;
	private static Economy economy;
	private static Essentials essentialsPlugin;

	
	public void onEnable() {
		long start = System.currentTimeMillis();

		PlayerManager.initialize();
		NationManager.initialize();

		SettlementManager.getInstance();
		NationManager nManager = NationManager.getInstance();
		nManager.loadNations();
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
		Settings.NationCreateCost = getConfig().getInt("NationCreateCost",1000);
		Settings.MinimumSettlementExposurePercent = (float) getConfig().getDouble("MinimumSettlementExposurePercent", 50.0);
		List<String> forbiddenCommandsInEnemyTerritory = getConfig().getStringList("forbiddenCommandsInEnemyTerritory");
		if (forbiddenCommandsInEnemyTerritory == null){
			Settings.forbiddenCommandsInEnemyTerritory = Collections.emptyList();
		} else {
			Settings.forbiddenCommandsInEnemyTerritory = forbiddenCommandsInEnemyTerritory;
		}
		Settings.Debug = getConfig().getBoolean("Debug", false);

		//Load plugins that are required for full functionality
		//Essentials
		Plugin essPlugin = getServer().getPluginManager().getPlugin("Essentials");
		if (essPlugin != null){
			if (essPlugin instanceof Essentials){
				getLogger().info("NationCraft found a compatible version of Essentials. Enabling Essentials integration.");
				essentialsPlugin = (Essentials) essPlugin;
			}
		}
		if (essentialsPlugin == null){
			getLogger().info("NationCraft did not find a compatible version of Essentials. Disabling Essentials integration");
		}
		//Vault
		if (getServer().getPluginManager().getPlugin("Vault") != null){
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp != null){
				getLogger().info("NationCraft found a compatible version of Vault. Enabling Vault integration.");
				economy = rsp.getProvider();
			} else {
				economy = null;
			}
		}
		if (economy == null){
			getLogger().info("NationCraft did not find a compatible version of Vault. Disabling Vault integration");
			Settings.nationBankMaxBalance = 0;
		} else {
			long maxNationBal = getConfig().getLong("nationBankMaxBalance", 1000000000);
			if (maxNationBal > Long.MAX_VALUE){
				getLogger().warning("nationBankMaxBalance in config.yml was set to " + maxNationBal + ", which may cause an overflow. Setting to maximum allowed value");
				maxNationBal = Long.MAX_VALUE;
			}
			Settings.nationBankMaxBalance = maxNationBal;
		}

		//register events
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new EntityListener(), this);

		//Now register commands
		this.getCommand("nation").setExecutor(new NationCommand());
		this.getCommand("map").setExecutor(new MapCommand());
		this.getCommand("chatmode").setExecutor(new ChatModeCommand());
		this.getCommand("settlement").setExecutor(new SettlementCommand());
		this.getCommand("nationcraft").setExecutor(new NationCraftCommand());
		long end = System.currentTimeMillis();
		getLogger().info(String.format("Took %d to enable", end - start));
	}

	@Override
	public void onDisable() {
		NationManager.getInstance().saveAllNationsToFile();

	}

	@Override
	public void onLoad() {
		instance = this;

	}
	
	public static synchronized NationCraft getInstance() {
		return instance;
	}

	public Economy getEconomy(){
		return economy;
	}

	public Essentials getEssentialsPlugin(){
		return essentialsPlugin;
	}
}
