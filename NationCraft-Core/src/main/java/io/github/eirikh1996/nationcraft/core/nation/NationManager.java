package io.github.eirikh1996.nationcraft.core.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationManager implements Runnable, Iterable<Nation> {
	private static NationManager ourInstance;
	private Map<String, Boolean> registeredFlags = new HashMap<>();
	private static File nationDir;
	private static NationCraftMain plugin;
	@NotNull private final Set<Nation> nations;

	private NationManager(){
		nations = new HashSet<>();
		registeredFlags.put("pvp", true);
		registeredFlags.put("powergain", true);
		registeredFlags.put("powerloss", true);
		registeredFlags.put("monsters", true);
		registeredFlags.put("open", false);
		registeredFlags.put("explosions", true);
		registeredFlags.put("firespread", true);
		registeredFlags.put("endergrief", false);
		registeredFlags.put("safezone", false);
		registeredFlags.put("warzone", false);
	}

	public static void initialize(File dataFolder, NationCraftMain plugin){
		ourInstance = new NationManager();
		NationManager.plugin = plugin;
		nationDir = new File(dataFolder, "nations");
	}

	public File getNationDir() {
		return nationDir;
	}

	public void loadNations(){
		nations.addAll(getNationsFromFile());
		createSafezone();
		createWarzone();
	}

	public Map<String, Boolean> getRegisteredFlags() {
		return registeredFlags;
	}

	/**
	 * Registers a new nation flag
	 *
	 * This is preferred to be done at onLoad in your plugin's main class
	 * @param flag The identifier of the flag
	 * @param def the default value of the flag
	 */
	public void registerFlag(String flag, boolean def) {
		registeredFlags.put(flag, def);
	}

	public boolean registeredFlag(String flag) {
		return registeredFlags.containsKey(flag);
	}

	public void reload(){
		nations.clear();
	    nations.addAll(getNationsFromFile());
    }

	public HashSet<Nation> getNationsFromFile(){
		if (!nationDir.exists()){
			nationDir.mkdirs();
		}
		HashSet<Nation> nations = new HashSet<>();
		File[] files = nationDir.listFiles();
		if (files == null) {
			return nations;
		}

		for (File file : files) {
			if (!file.isFile()) {
				continue;
			}
			if (!file.getName().contains(".nation")) {
				continue;
			}
			Nation n = new Nation(file);
			nations.add(n);
		}
        plugin.logInfo("Nations loaded: ");
		for (Nation nation : nations){
			plugin.logInfo(nation.getName());
        }
		return nations;
	}
	public Nation getNationBySettlement(Settlement settlement){
		for (Nation n : this){
			if (n.getSettlements().contains(settlement) || n.getCapital() != null && n.getCapital().equals(settlement)){
				return n;
			}
		}
		return null;
	}

	public Nation getNationAt(Territory territory){
		Nation returnNation = null;
		for (Nation nation : this){
			if (nation.getTerritoryManager().contains(territory)){
				returnNation = nation;
				break;
			}
		}
		return returnNation;
	}

	public Nation getNationByPlayer(UUID id){
		for (Nation nation : nations){
			if (!nation.hasPlayer(id)){
				continue;
			}
			return nation;
		}
		return null;
	}

	public Nation getNationByPlayer(NCPlayer player){
		for (Nation nation : nations){
			if (!nation.hasPlayer(player)){
				continue;
			}
			return nation;
		}
		return null;
	}
	public Nation getNationAt(String world, int x, int z){
		Nation returnNation = null;
		for (Nation nation : nations){
			for (Territory terr : nation.getTerritoryManager()){
				if (terr.getX() != x||terr.getZ() != z || !terr.getWorld().equals(world))
					continue;
				returnNation = nation;
				break;
			}
			if (returnNation == nation)
				break;
		}
		return returnNation;
	}

	public File getNationFile(String nationName){
		return new File(nationDir, nationName.toLowerCase() + ".nation");
	}

	@NotNull
	public Set<Nation> getNations() {
		return nations;
	}

	public Nation getNationByName(String name){
		Nation returnNation = null;
		for (Nation n : nations){
			if (n.getName().equalsIgnoreCase(name)){
				returnNation = n;
			}
		}
		return returnNation;
	}




	public TextColor getColor(@NotNull NCPlayer p, @NotNull Nation n){
		TextColor returnColor = TextColor.RESET;
		Nation pNation = NationManager.getInstance().getNationByPlayer(p.getPlayerID());
		if (pNation == null){
		    returnColor = TextColor.WHITE;
        }
		else if (pNation == n){
			returnColor = TextColor.GREEN;
		}
		else if (pNation.getRelationTo(n) == Relation.ENEMY){
			returnColor = TextColor.RED;
		}
		else if (pNation.getRelationTo(n) == Relation.ALLY){
			returnColor = TextColor.DARK_PURPLE;
		}
		else if (pNation.getRelationTo(n) == Relation.NEUTRAL){
		    returnColor = TextColor.WHITE;
		}
		if (n.getOriginalName().equalsIgnoreCase("Warzone")){
		    returnColor = TextColor.DARK_RED;
        } else if (n.getOriginalName().equalsIgnoreCase("Safezone")){
		    returnColor = TextColor.GOLD;
        }
		return returnColor;
	}

	private void createSafezone(){

		Nation safezone = getNationByName("Safezone");
		if (safezone != null) {
			return;
		}
		safezone = new Nation("Safezone","Free from PvP and monsters");
		safezone.setSafezone(true);
		safezone.setPvPAllowed(false);
		safezone.setMonstersAllowed(false);
		addNation(safezone);
	}

	private void createWarzone(){
		Nation warzone = getNationByName("Warzone");
		if (warzone != null) {
			return;
		}
		warzone = new Nation("Warzone","Not the safest place to be!");
		warzone.setWarzone(true);
		addNation(warzone);
	}

	public static NationManager getInstance(){
		return ourInstance;
	}

	public void saveAllNationsToFile(){
		for (Nation n : this){
			if (n == null || n.getPlayers().isEmpty() && !n.isWarzone() && !n.isSafezone()){
				continue;
			}
			n.saveToFile();
		}
	}


	@NotNull
	@Override
	public Iterator<Nation> iterator() {
		return Collections.unmodifiableSet(this.nations).iterator();
	}


    @Override
    public void run() {
        processNationCleanup();
    }

	/**
	 * Creates a new nation and returns the resulting nation if creation succeeds
	 * @param name The name of the nation
	 * @param founder The founder of the nation
	 * @return the created nation if creation succeeds, and null if it fails
	 */
	@Nullable
    public Nation createNation(String name, @NotNull NCPlayer founder) {
		Nation n = new Nation(name, founder);
		if (getNationByName(name) != null) {
			founder.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "A nation called " + name + " already exists");
			return null;
		}
		final NationCreateEvent event = new NationCreateEvent(n, founder);
		NationCraftAPI.getInstance().callEvent(event);
		if (event.isCancelled()) {
			return null;
		}
		addNation(n);
		return n;
	}

    private void processNationCleanup() {
	    if (nations.isEmpty())
	        return;
	    final Iterator<Nation> iterator = nations.iterator();
	    while (iterator.hasNext()) {
	        final Nation n = iterator.next();
	        if (n == null || !n.getPlayers().isEmpty() || n.isSafezone() || n.isWarzone()) {
	            continue;
            }

	        final File nFile = getNationFile(n.getName());
            plugin.broadcast(NATIONCRAFT_COMMAND_PREFIX + String.format("Nation %s has been disbanded", n.getName()));
	        nFile.delete();
	        iterator.remove();

        }
    }

	public void addNation(Nation newNation) {
		nations.add(newNation);
		newNation.saveToFile();
	}
}
