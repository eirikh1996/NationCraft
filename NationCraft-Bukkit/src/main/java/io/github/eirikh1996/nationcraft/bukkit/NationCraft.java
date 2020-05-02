package io.github.eirikh1996.nationcraft.bukkit;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.bukkit.hooks.chat.VentureChatHook;
import io.github.eirikh1996.nationcraft.bukkit.objects.NCBukkitConsole;
import io.github.eirikh1996.nationcraft.bukkit.player.BukkitPlayerManager;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.bukkit.listener.BlockListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.ChatListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.EntityListener;
import io.github.eirikh1996.nationcraft.bukkit.listener.PlayerListener;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.bukkit.utils.PlaceHolderUtils;
import io.github.eirikh1996.nationcraft.core.Core;
import io.github.eirikh1996.nationcraft.core.commands.CommandRegistry;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.nation.Relation;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import mineverse.Aust1n46.chat.MineverseChat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
import java.util.Optional;

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
		Messages.initialize(this);
		NationManager nManager = NationManager.getInstance();
		nManager.loadNations();
		if (nManager.getNations().isEmpty()){
			getLogger().info("No nation files loaded");
		}
		else {
			getLogger().info(String.format("Loaded %d nation files", NationManager.getInstance().getNations().size()));
		}
		getLogger().info(nManager.getNations().toString());
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
		getServer().getScheduler().runTaskTimerAsynchronously(this, nManager, 0, 20);



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
			new PlaceHolderUtils().register();
		}
		if (placeholderAPIPlugin == null) {
			getLogger().info("NationCraft did not find a compatible version of PlaceholderAPI. Disabling PlaceholderAPI integration");
		}
		Plugin vChat = getServer().getPluginManager().getPlugin("VentureChat");
		if (vChat instanceof MineverseChat) {
			getLogger().info("NationCraft found a compatible version of VentureChat. Enabling VentureChat integration");
			Settings.UseExternalChatPlugin = true;
			getServer().getPluginManager().registerEvents(new VentureChatHook(), this);
		}
		readConfig();

		//register events
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new EntityListener(), this);

		registerCoreListeners();

		for (io.github.eirikh1996.nationcraft.core.commands.Command cmd : commandRegistry) {
			final PluginCommand command = getCommand(cmd.getName());
			if (command == null) {
				continue;
			}
			command.setAliases(cmd.getAliases());
		}

		//Now register commands
		commandRegistry.registerDefaultCommands();
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
		commandRegistry = new CommandRegistry();
		Core.initialize(this);
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
		saveDefaultConfig();

		//Read config file
		//player
		ConfigurationSection playerSection = getConfig().getConfigurationSection("Player");
		Settings.player.chatFormat = playerSection.getString("ChatFormat", "(%NATION%)(%SETTLEMENT%)");
		Settings.player.MaxDaysInactivity = playerSection.getInt("MaxDaysInactivity", 25);
		Settings.player.MaxPower = playerSection.getDouble("MaxPower", 100.0);
		Settings.player.InitialPower = playerSection.getDouble("InitialPower", 10.0);
		Settings.player.RegeneratePowerOffline = playerSection.getBoolean("RegenerateOffline");
		Settings.player.PowerPerHour = playerSection.getDouble("PowerPerHour", 2.0);
		Settings.player.PowerPerDeath = playerSection.getDouble("PowerPerDeath", -2.0);
		Settings.player.RegeneratePowerOffline = playerSection.getBoolean("RegenerateOffline", false);
		Settings.player.TeleportationWarmup = playerSection.getInt("TeleportationWarmup", 10);
		Settings.player.TeleportationCooldown = playerSection.getInt("TeleportationCooldown", 60);

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
	public String getVersion() {
		return getDescription().getVersion();
	}

	@Override
	public List<String> getAuthors() {
		return getDescription().getAuthors();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
		String cmd = command.getName();
		if (!commandRegistry.isRegistered(cmd)) {
			return false;
		}
		commandRegistry.runCommand(cmd, BukkitUtils.getInstance().getCorrespondingSender(sender), args);
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (!commandRegistry.isRegistered(command.getName())) {
			return super.onTabComplete(sender, command, alias, args);
		}
		io.github.eirikh1996.nationcraft.core.commands.Command cmd = commandRegistry.getCommand(command.getName());
		List<String> completions = new ArrayList<>();
		final NCCommandSender ncSender = BukkitUtils.getInstance().getCorrespondingSender(sender);
		Optional<io.github.eirikh1996.nationcraft.core.commands.Command> child = cmd.getChild(args[0]);
		if (child.isPresent()) {
			for (String arg : child.get().getTabCompletions(ncSender, args)) {
				if (!arg.startsWith(args[args.length - 1]))
					continue;
				completions.add(arg);
			}
			return completions;
		}
		for (String arg : cmd.getTabCompletions(ncSender, args)) {
			if (!arg.startsWith(args[args.length - 1]))
				continue;
			completions.add(arg);
		}
		return completions;
	}
}
