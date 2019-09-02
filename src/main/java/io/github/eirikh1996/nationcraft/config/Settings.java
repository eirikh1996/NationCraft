package io.github.eirikh1996.nationcraft.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Settings {
	public static long nationBankMaxBalance = 1000000000;
	public static int maxStrengthPerPlayer = 30;
	public static int NationCreateCost;
	public static int maxAllies = -1;
	public static int maxPlayersPerNation = 50;
	@NotNull public static List<String> forbiddenNames = null; //Names that are forbidden to use for nations or settlements
	public static int reduceStrengthOnDeath = 1;
	public static List<String> reduceStrengthInWorlds = new ArrayList<>();
	public static List<String> forbiddenCommandsInEnemyTerritory = new ArrayList<>();


    public static boolean Debug;
}
