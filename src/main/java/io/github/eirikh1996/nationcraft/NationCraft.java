package io.github.eirikh1996.nationcraft;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.commands.*;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.listener.BlockListener;
import io.github.eirikh1996.nationcraft.listener.ChatListener;
import io.github.eirikh1996.nationcraft.listener.EntityListener;
import io.github.eirikh1996.nationcraft.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Relation;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
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
	private static PlaceholderAPIPlugin placeholderAPIPlugin;
	private static ICombatLogX combatLogXPlugin;
	
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
		File szFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/safezone.nation");
		File wzFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/warzone.nation");
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
			getLogger().info("Warzone file created.");
			else
			getLogger().warning("Warzone failed to create file!");
		}
		if (wzCreated || szCreated){
			nManager.reload();
		}

		//Load config file
		this.saveDefaultConfig();

		//Read config file
		//player
		ConfigurationSection playerSection = getConfig().getConfigurationSection("Player");
		Settings.PlayerMaxDaysInactivity = playerSection.getInt("MaxDaysInactivity", 25);
		Settings.PlayerMaxPower = playerSection.getDouble("MaxPower", 100.0);
		Settings.PlayerInitialPower = playerSection.getDouble("InitialPower", 10.0);
		Settings.PlayerRegeneratePowerOffline = playerSection.getBoolean("RegenerateOffline");
		Settings.PlayerPowerPerHour = playerSection.getDouble("PowerPerHour", 2.0);
		Settings.PlayerPowerPerDeath = playerSection.getDouble("PowerPerDeath", -2.0);
		Settings.PlayerRegeneratePowerOffline = playerSection.getBoolean("RegenerateOffline", false);
		Settings.PlayerTeleportationWarmup = playerSection.getInt("TeleportationWarmup", 10);
		Settings.PlayerTeleportationCooldown = playerSection.getInt("TeleportationCooldown", 60);

		//nation
		ConfigurationSection nationSection = getConfig().getConfigurationSection("Nation");
		Settings.NationBankMaxBalance = nationSection.getLong("BankMaxBalance", 1000000000);
		Settings.NationMaxAllies = nationSection.getInt("MaxAllies", -1);
		Settings.NationMaxTruces = nationSection.getInt("MaxTruces", -1);
		Settings.NationMaxPlayers = nationSection.getInt("MaxPlayers", 50);
		Settings.NationCreateCost = nationSection.getInt("CreateCost",1000);
		Settings.NationMinimumRequiredPlayers = nationSection.getInt("MinimumRequiredPlayers", 5);
		Settings.NationDeleteAfterDays = nationSection.getInt("DeleteAfterDays", 14);
		Settings.NationForbiddenNames.addAll(nationSection.getStringList("ForbiddenNames"));
		final ConfigurationSection forbiddenCommands = nationSection.getConfigurationSection("ForbiddenCommands");
		for (Relation rel : Relation.values()) {
			String key = rel.name().replace(rel.name().substring(1), rel.name().substring(1).toLowerCase());
			List<String> values = forbiddenCommands.getStringList(key);
			Settings.NationForbiddenCommands.put(rel, values);
		}

		//settlements
		ConfigurationSection settlementSection = getConfig().getConfigurationSection("Settlement");
		Settings.MinimumSettlementExposurePercent = (float) getConfig().getDouble("MinimumSettlementExposurePercent", 50.0);


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
			Settings.NationBankMaxBalance = 0;
		} else {
			Settings.NationBankMaxBalance = nationSection.getLong("BankMaxBalance", 1000000000);
		}

		//Placeholder API
		Plugin pHolder = getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (pHolder instanceof PlaceholderAPIPlugin) {
			getLogger().info("NationCraft found a compatible version of PlaceholderAPI. Enabling PlaceholderAPI integration");
			placeholderAPIPlugin = (PlaceholderAPIPlugin) pHolder;
		}
		if (placeholderAPIPlugin == null) {
			getLogger().info("NationCraft didn not find a compatible version of PlaceholderAPI. Disabling PlaceholderAPI integration");
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
		Messages.logMessage();
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

	public PlaceholderAPIPlugin getPlaceholderAPIPlugin() {
		return placeholderAPIPlugin;
	}

	public ICombatLogX getCombatLogXPlugin() {
		return combatLogXPlugin;
	}
}
