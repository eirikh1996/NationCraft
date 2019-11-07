package io.github.eirikh1996.nationcraft.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Settings {
	public static long nationBankMaxBalance = 1000000000;
	public static double maxPowerPerPlayer = 30.0;
	public static double powerPerHour = 2.0;
	public static boolean regeneratePowerOffline = false;
	public static double initialPowerPerPlayer = 10.0;
	public static int NationCreateCost;
	public static int maxAllies = -1;
	public static int maxPlayersPerNation = 50;
	@NotNull public static List<String> forbiddenNames = null; //Names that are forbidden to use for nations or settlements
	public static double reducePowerOnDeath = -1.0;
	public static int teleportationWarmupTime = 5;
	public static int teleportationCooldownTime = 60;
	public static List<String> reducePowerInWorlds = new ArrayList<>();
	public static List<String> forbiddenCommandsInEnemyTerritory = new ArrayList<>();
	public static int MaxDaysInactivity;

	public static boolean Debug;
    public static float MinimumSettlementExposurePercent;
}
