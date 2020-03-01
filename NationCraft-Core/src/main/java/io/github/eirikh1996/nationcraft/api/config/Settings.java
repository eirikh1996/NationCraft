package io.github.eirikh1996.nationcraft.api.config;

import io.github.eirikh1996.nationcraft.api.nation.Relation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

	//Player settings
	public static double PlayerMaxPower = 30.0;
	public static double PlayerPowerPerHour = 2.0;
	public static double PlayerInitialPower = 10.0;
	public static double PlayerPowerPerDeath = -2.0;
	public static int PlayerMaxDaysInactivity = 20;
	public static boolean PlayerRegeneratePowerOffline = false;
	public static int PlayerTeleportationWarmup = 10;
	public static int PlayerTeleportationCooldown = 60;

	//Nation settings
	public static long NationBankMaxBalance = 1000000000;
	public static int NationCreateCost;
	public static int NationMaxAllies = -1;
	public static int NationMaxTruces = -1;
	public static int NationMaxPlayers = 50;
	public static int NationMinimumRequiredPlayers = 5;
	public static int NationDeleteAfterDays = 14;
	public static Map<Relation, List<String>> NationForbiddenCommands = new HashMap<>();
	@NotNull public static List<String> NationForbiddenNames = new ArrayList<>(); //Names that are forbidden to use for nations or settlements
	public static List<String> reducePowerInWorlds = new ArrayList<>();


	public static boolean Debug;
    public static float MinimumSettlementExposurePercent;
    public static int SiegeRequiredAttackerPresenceTime;
	public static int SiegeMaximumAttackerAbsenceTime;


}
