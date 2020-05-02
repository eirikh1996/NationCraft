package io.github.eirikh1996.nationcraft.api.config;

import io.github.eirikh1996.nationcraft.api.nation.Relation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
	public static final PlayerSettings player;
	public static final NationSettings nation;
	public static final SettlementSettings settlement;
	static {
		player = new PlayerSettings();
		nation = new NationSettings();
		settlement = new SettlementSettings();
	}
	public static class PlayerSettings {
		private PlayerSettings() {

		}
		public double MaxPower = 30.0;
		public double PowerPerHour = 2.0;
		public double InitialPower = 10.0;
		public double PowerPerDeath = -2.0;
		public int MaxDaysInactivity = 20;
		public boolean RegeneratePowerOffline = false;
		public int TeleportationWarmup = 10;
		public int TeleportationCooldown = 60;
		public List<String> reducePowerInWorlds = new ArrayList<>();
		public String chatFormat = "";
	}
	private static class NationSettings {

	}
	public static class SettlementSettings {
		public int TerritoryPerPlayer = 10;

	}


	//Player settings


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



	public static boolean Debug;
    public static float MinimumSettlementExposurePercent;
    public static int SiegeRequiredAttackerPresenceTime;
	public static int SiegeMaximumAttackerAbsenceTime;
	public static boolean UseExternalChatPlugin = false;


}
