package io.github.eirikh1996.nationcraft.api.config;

import io.github.eirikh1996.nationcraft.core.nation.Relation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Settings {
	public static final PlayerSettings player;
	public static final SettlementSettings settlement;
	static {
		player = new PlayerSettings();
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
	public static class SettlementSettings {
		public int TerritoryPerPlayer = 10;
		public int NationTerritoryBonus = 10;

	}


	//Player settings






	public static boolean Debug;
    public static float MinimumSettlementExposurePercent;
    public static int SiegeRequiredAttackerPresenceTime;
	public static int SiegeMaximumAttackerAbsenceTime;
	public static boolean UseExternalChatPlugin = false;


}
