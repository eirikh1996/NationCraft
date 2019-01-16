package io.github.eirikh1996.nationcraft.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.eirikh1996.nationcraft.events.NationCreateEvent;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;

public class NationCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (cmd.getName().equalsIgnoreCase("nation")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Error: You must be a player to execute this command!");
				return true;
			}
			if (args.length < 1){
				sender.sendMessage("Type /nation help for halp on the nation command");
				return true;
			}
			if (args[0].equalsIgnoreCase("create")){
				if (args.length < 2){
					sender.sendMessage("You need to specify a name for your nation");
					return false;
				}
				createNation(sender, args[1]);
				return true;
			}
			if (args[0].equalsIgnoreCase("ally")){
				if (args[1].length() < 1){
					sender.sendMessage("You need to specify a nation");
					return false;
				}
				allyNationCommand(sender, args[1]);
				return true;
			}
			if (args[0].equalsIgnoreCase("neutral")){
				if (args[1].length() < 1){
					sender.sendMessage("You need to specify a nation");
					return false;
				}
				neutralNationCommand(sender, args[1]);
				return true;
			}
			if (args[0].equalsIgnoreCase("war")){

			}
			if (args[0].equalsIgnoreCase("disband")){

			}
			if (args[0].equalsIgnoreCase("info")){

			}
		}
		
		return false;
	}

	private void createNation(CommandSender sender, String name){
		if (!sender.hasPermission("nationcraft.nation.create")) {
			sender.sendMessage("Error: You do not have permission to use this command!");
			return;
		}
		if (name.length() < 2) {
			sender.sendMessage("Error: Nation names must be at least 2 characters long");
			return;
		}
		for (Nation existing : NationManager.getInstance().getNations()){
			if (existing.getName().equalsIgnoreCase(name)){
				sender.sendMessage(String.format("Error: A nation with the name of %s already exists!", name));
				return;
			}
		}
		String description = "Default description";
		List<Chunk> territory = new ArrayList<>();
		String capital = "(none)";
		List<String> settlements = new ArrayList<>();
		List<String> allies = new ArrayList<>();
		List<String> enemies = new ArrayList<>();
		Map<Player, Ranks> players = new HashMap<>();
		players.put((Player) sender,Ranks.LEADER);

		Nation newNation = new Nation(name, description, capital, allies, enemies, settlements, territory, players);
		//Call event
		NationCreateEvent event = new NationCreateEvent(newNation, (Player) sender);
		if (event.isCancelled()){
			return;
		}
		NationManager.getInstance().getNations().add(newNation);

	}
	private void allyNationCommand(CommandSender sender, String name){
		if (!sender.hasPermission("nationcraft.nation.ally")){
			sender.sendMessage("Error: You do not have permission to use this command!");
		}
		NationManager nMgr = new NationManager();

		Nation ownNation = nMgr.getNationByPlayer((Player) sender); //sender's own nation
		Nation allyNation = nMgr.getNationByName(name); //nation to ally
		if (allyNation == null){
			sender.sendMessage("The given nation does not exist");
			return;
		}
		if (ownNation.isAlliedWith(allyNation.getName())){
			sender.sendMessage("You have already set this relation wish with " + allyNation.getName());
			return;
		}
		if (allyNation.getAllies().contains(ownNation.getName())){
			sender.sendMessage(allyNation.getName() + "is now an allied nation");
		} else {
			for (Player p : allyNation.getPlayers().keySet()){
				p.sendMessage(ownNation.getName() + " wishes to be an allied nation. Use command /nation ally " + ownNation.getName() + " to confirm an alliance.");
			}
		}
		ownNation.addAlly(allyNation.getName());
	}
	private void neutralNationCommand(CommandSender sender, String name){
		NationManager nMgr = new NationManager();
		Nation ownNation = nMgr.getNationByPlayer((Player) sender); //sender's own nation
		Nation neutralNation = nMgr.getNationByName(name); //nation to ally
		if (ownNation.getAllies().contains(neutralNation.getName())){
			ownNation.removeAlly(neutralNation.getName());
			//if the sender's nation is on the neutral nations enemy list
			if (neutralNation.getEnemies().contains(ownNation.getName()) ){
				for (Player p : neutralNation.getPlayers().keySet()){
					p.sendMessage(ownNation.getName() + " wishes to be a neutral nation. Type /nation neutral " + ownNation.getName() + " to confirm neutrality.");
				}
				ownNation.removeEnemy(neutralNation.getName());
			}  else {
				sender.sendMessage(neutralNation.getName() + " is now a neutral nation");
			}
			//&& !neutralNation.getAllies().contains(ownNation.getName())
		} else if (ownNation.getEnemies().contains(neutralNation.getName())){
			ownNation.removeEnemy(neutralNation.getName());

		} else {
			sender.sendMessage(neutralNation.getName() + " is already neutral");
			return;
		}


	}
	
}
