package io.github.eirikh1996.nationcraft.bukkit;

import com.earth2me.essentials.Essentials;
import com.github.sirblobman.combatlogx.api.ICombatLogX;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.config.WorldSettings;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.territory.TerritoryManager;
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
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.nation.Relation;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import mineverse.Aust1n46.chat.MineverseChat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

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
		TerritoryManager.getInstance().initialize(this);
		Messages.initialize(this);
		NationManager nManager = NationManager.getInstance();
		nManager.loadNations();
		if (nManager.getNations().isEmpty()){
			getLogger().info("No nation files loaded");
		}
		else {
			getLogger().info(String.format("Loaded %d nation files", NationManager.getInstance().getNations().size()));
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
		commandRegistry.registerDefaultCommands();
		Map<String, Map<String, Object>> newCommandMap = new HashMap<>(getDescription().getCommands());
		commandRegistry.getRegisteredCommands().forEach((str, cmd) -> {
			final Map<String, Object> serializedCommand = new HashMap<>();
			serializedCommand.put("aliases", cmd.getAliases());
			newCommandMap.put(str, serializedCommand);
		});
        try {
            final Field commandsField = PluginDescriptionFile.class.getDeclaredField("commands");
			commandsField.setAccessible(true);
			commandsField.set(getDescription(), newCommandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

		//world
		ConfigurationSection worldSection = getConfig().getConfigurationSection("World");
		WorldSettings.powerGainEnabled = worldSection.getStringList("PowerGainEnabled");
		WorldSettings.powerLossEnabled = worldSection.getStringList("PowerLossEnabled");
		//nation
		ConfigurationSection nationSection = getConfig().getConfigurationSection("Nation");
		NationSettings.BankMaxBalance = nationSection.getLong("BankMaxBalance", 1000000000);
		NationSettings.MaxAllies = nationSection.getInt("MaxAllies", -1);
		NationSettings.MaxTruces = nationSection.getInt("MaxTruces", -1);
		NationSettings.MaxPlayers = nationSection.getInt("MaxPlayers", 50);
		NationSettings.CreateCost = nationSection.getInt("CreateCost",1000);
		NationSettings.MinimumRequiredPlayers = nationSection.getInt("MinimumRequiredPlayers", 5);
		NationSettings.DeleteAfterDays = nationSection.getInt("DeleteAfterDays", 14);
		NationSettings.ForbiddenNames.addAll(nationSection.getStringList("ForbiddenNames"));
		final ConfigurationSection forbiddenCommands = nationSection.getConfigurationSection("ForbiddenCommands");
		for (String s : forbiddenCommands.getValues(true).keySet()) {
			Relation rel = Relation.getRelationIgnoreCase(s);
			if (rel == null) {
				getLogger().severe(s + " is not a valid relation value");
				continue;
			}
			List<String> values = (List<String>) forbiddenCommands.getValues(true).get(s);
			NationSettings.ForbiddenCommands.put(rel, values);
		}
		final ConfigurationSection damageReductionPercentage = nationSection.getConfigurationSection("DamageReductionPercentage");
		for (String s : damageReductionPercentage.getValues(true).keySet()) {
			final Relation rel = Relation.getRelationIgnoreCase(s);
			if (rel == null) {
				getLogger().severe(s + " is not a valid relation value");
				continue;
			}
			double val = (double) damageReductionPercentage.getValues(true).get(s);
			NationSettings.DamageReductionPercentage.put(rel, val);
		}
		final List<String> disableDamageFor = nationSection.getStringList("DisableDamageFor");
		for (String str : disableDamageFor) {
			NationSettings.DisableDamageFor.add(Relation.getRelationIgnoreCase(str));
		}
		final ConfigurationSection relColors = nationSection.getConfigurationSection("RelationColors");
		for (String key : relColors.getValues(true).keySet()) {
			NationSettings.RelationColors.put(Relation.getRelationIgnoreCase(key), TextColor.getColorIgnoreCase((String) relColors.getValues(true).get(key)));
		}

		//settlements
		ConfigurationSection settlementSection = getConfig().getConfigurationSection("Settlement");
		Settings.MinimumSettlementExposurePercent = (float) getConfig().getDouble("MinimumSettlementExposurePercent", 50.0);


		Settings.Debug = getConfig().getBoolean("Debug", false);

		if (economy == null){
			getLogger().info("NationCraft did not find a compatible version of Vault. Disabling Vault integration");
			NationSettings.BankMaxBalance = 0;
		} else {
			NationSettings.BankMaxBalance = nationSection.getLong("BankMaxBalance", 1000000000);
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
