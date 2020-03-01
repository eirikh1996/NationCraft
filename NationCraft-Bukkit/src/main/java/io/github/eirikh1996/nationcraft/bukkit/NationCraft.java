package io.github.eirikh1996.nationcraft.bukkit;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.bukkit.objects.NCBukkitConsole;
import io.github.eirikh1996.nationcraft.bukkit.player.BukkitPlayerManager;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.bukkit.listener.BlockListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.ChatListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.EntityListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.core.commands.CommandRegistry;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.nation.Relation;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NationCraft extends JavaPlugin implements NationCraftMain {
	private static Economy economy;
	private static Essentials essentialsPlugin;
	private static PlaceholderAPIPlugin placeholderAPIPlugin;
	private static ICombatLogX combatLogXPlugin;
	private static NationCraftAPI api;
	private static CommandRegistry commandRegistry;
	private static NationCraft instance;

	public void onEnable() {
		long start = System.currentTimeMillis();

		BukkitPlayerManager.initialize(this);
		NationManager.initialize(getDataFolder(),this);

		SettlementManager.initialize(this);
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

		for (io.github.eirikh1996.nationcraft.core.commands.Command cmd : commandRegistry) {
			final PluginCommand command = getCommand(cmd.getName());
			if (command == null) {
				continue;
			}
			command.setAliases(cmd.getAliases());
		}

		//Now register commands
		/*this.getCommand("nation").setExecutor(new NationCommand());
		this.getCommand("player").setExecutor(new PlayerCommand());
		this.getCommand("map").setExecutor(new MapCommand());
		this.getCommand("chatmode").setExecutor(new ChatModeCommand());
		this.getCommand("settlement").setExecutor(new SettlementCommand());
		this.getCommand("nationcraft").setExecutor(new NationCraftCommand());*/
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
		api = NationCraftAPI.getInstance();
		try {
			Class clazz = Class.forName("io.github.eirikh1996.nationcraft.core.commands.CommandRegistry");
			if (CommandRegistry.class.isAssignableFrom(clazz)) {
				final Constructor c = clazz.getConstructor();
				if (!c.isAccessible()) {
					c.setAccessible(true);
				}
				commandRegistry = (CommandRegistry) c.newInstance();
			}
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
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

	@Override
	public void logError(String errorMessage) {
		getLogger().severe(errorMessage);
	}

	@Override
	public void logWarning(String warningMessage) {
		getLogger().warning(warningMessage);
	}

	@Override
	public void logInfo(String infoMessage) {
		getLogger().info(infoMessage);
	}

	@Override
	public void readConfig() {
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

		if (economy == null){
			getLogger().info("NationCraft did not find a compatible version of Vault. Disabling Vault integration");
			Settings.NationBankMaxBalance = 0;
		} else {
			Settings.NationBankMaxBalance = nationSection.getLong("BankMaxBalance", 1000000000);
		}
	}

	@Override
	public void broadcast(String message) {
		getServer().broadcastMessage(message);
	}

	@Override
	public NCConsole getConsole() {
		return new NCBukkitConsole(getServer().getConsoleSender());
	}

	@Override
	public NationCraftAPI getAPI() {
		return api;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName();
		if (!commandRegistry.isRegistered(cmd)) {
			return false;
		}
		commandRegistry.runCommand(cmd, BukkitUtils.getInstance().getCorrespondingSender(sender), args);
		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (!commandRegistry.isRegistered(command.getName())) {
			return super.onTabComplete(sender, command, alias, args);
		}
		io.github.eirikh1996.nationcraft.core.commands.Command cmd = commandRegistry.getCommand(command.getName());
		List<String> completions = new ArrayList<>();
		for (String arg : cmd.getTabCompletions()) {
			if (!args[args.length - 1].startsWith(arg))
				continue;
			completions.add(arg);
		}
		return completions;
	}
}
