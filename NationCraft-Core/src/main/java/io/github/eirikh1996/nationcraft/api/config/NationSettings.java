package io.github.eirikh1996.nationcraft.api.config;

import io.github.eirikh1996.nationcraft.core.nation.Relation;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NationSettings {
    //Nation settings
    public static long BankMaxBalance = 1000000000;
    public static int CreateCost;
    public static int MaxAllies = -1;
    public static int MaxTruces = -1;
    public static int MaxPlayers = 50;
    public static int MinimumRequiredPlayers = 5;
    public static int DeleteAfterDays = 14;
    @NotNull public static Map<Relation, Double> DamageReductionPercentage = new HashMap<>();
    @NotNull public static Set<Relation> DisableDamageFor = new HashSet<>();
    @NotNull public static Map<Relation, List<String>> ForbiddenCommands = new HashMap<>();
    @NotNull public static List<String> ForbiddenNames = new ArrayList<>(); //Names that are forbidden to use for nations or settlements
    @NotNull public static Map<Relation, NamedTextColor> RelationColors = new HashMap<>();
}
