package io.github.eirikh1996.nationcraft.commands;

import java.util.*;

import io.github.eirikh1996.nationcraft.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import io.github.eirikh1996.nationcraft.utils.TopicPaginator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;

public class NationCommand implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (!cmd.getName().equalsIgnoreCase("nation")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("Error: You must be a player to execute this command!");
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage("Type /nation help for halp on the nation command");
			return true;
		}
		if (args[0].equalsIgnoreCase("create")){
			if (args.length < 2){
				sender.sendMessage(Messages.ERROR + "You must specify a name!");
			} else{
				createNationCommand((Player) sender, args[1]);
			}
		}
		else if (args[0].equalsIgnoreCase("ally")){
			if (args[1].length() < 1){
				sender.sendMessage("You need to specify a nation");
				return true;
			}
			allyNationCommand(sender, args[1]);
			return true;
		}
		else if (args[0].equalsIgnoreCase("neutral")){
			if (args[1].length() < 1){
				sender.sendMessage("You need to specify a nation");
				return true;
			}
			neutralNationCommand(sender, args[1]);
			return true;
		}
		else if (args[0].equalsIgnoreCase("war")){

		}
		else if (args[0].equalsIgnoreCase("disband")){

		}
		//leave
		else if (args[0].equalsIgnoreCase("info")){
			Nation n;
			if (args.length < 2){
				n = NationManager.getInstance().getNationByPlayer((Player) sender);
			} else {
				n = NationManager.getInstance().getNationByName(args[1]);
			}
			if (n == null){
				sender.sendMessage(Messages.ERROR + "Nation does not exist!");
				return true;
			}
			Messages.nationInfo(n, (Player) sender, ChatColor.GREEN);
			return true;
		}
		//join
		else if (args[0].equalsIgnoreCase("join")){
			if (!sender.hasPermission("nationcraft.nation.join")){
				sender.sendMessage(Messages.ERROR + Messages.NO_PERMISSION);
			}
			if (args.length == 1){
				sender.sendMessage(Messages.ERROR + "You must specify the nation you wish to join");
			} else {
				joinNationCommand((Player) sender,args[1]);
			}

		} else if (args[0].equalsIgnoreCase("leave")){
			if (!sender.hasPermission("nationcraft.nation.leave")){
				sender.sendMessage(Messages.ERROR + Messages.NO_PERMISSION);
				return true;
			}
			leaveNationCommand((Player) sender);

		} else if (args[0].equalsIgnoreCase("list")){
			if (args.length == 1){
				listNationsCommand((Player) sender, 1);
				return true;
			}
			listNationsCommand((Player) sender,Integer.parseInt(args[1]));
		}
		return true;
	}

	//create
	private void createNationCommand(Player sender, String name){
		if (!sender.hasPermission("nationcraft.nation.create")) {
			sender.sendMessage("Error: You do not have permission to use this command!");
			return;
		}
		if (name.length() <= 2) {
			sender.sendMessage("Error: Nation names must be at least 2 characters long");
			return;
		}
		for (Nation existing : NationManager.getInstance().getNations()){
			if (existing.getName().equalsIgnoreCase(name)){
				sender.sendMessage(String.format("Error: A nation with the name of %s already exists!", name));
				return;
			}
		}
		if (NationManager.getInstance().getNationByPlayer(sender) != null){
			sender.sendMessage(Messages.ERROR + "You must leave your current nation before you can create one!");
			return;
		}
		String description = "Default description";
		List<Chunk> territory = new ArrayList<>();
		String capital = "(none)";
		List<String> settlements = new ArrayList<>();
		List<String> allies = new ArrayList<>();
		List<String> enemies = new ArrayList<>();
		Map<UUID, Ranks> players = new HashMap<>();
		players.put(((Player) sender).getUniqueId(),Ranks.LEADER);

		Nation newNation = new Nation(name, description, capital, allies, enemies, settlements, territory, players);
		//Call event
		NationCreateEvent event = new NationCreateEvent(newNation, sender);
		NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()){
			sender.sendMessage("Cancelled: " + event.isCancelled());
			return;
		}
		sender.sendMessage("You successfully created a new nation named " + ChatColor.GREEN + name);
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
			for (UUID id : allyNation.getPlayers().keySet()){
				Player p = Bukkit.getPlayer(id);
				if (p == null){
					continue;
				}
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
				for (UUID id : neutralNation.getPlayers().keySet()){
					Player p = Bukkit.getPlayer(id);
					if (p == null){
						continue;
					}
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
	private void listNationsCommand(Player p, int page){
		if (NationManager.getInstance().getNations().isEmpty()){
			p.sendMessage(Messages.ERROR + "No nations found");
			return;
		}
		TopicPaginator paginator = new TopicPaginator("Nations");
		for (Nation n : NationManager.getInstance().getNations()){
			String nationName = NationManager.getInstance().getColor(p,n) + n.getName() + ChatColor.YELLOW;
			paginator.addLine(String.format("%s: Players: %d, Settlements: %d, Capital: %s", nationName,n.getPlayers().keySet().size(),n.getSettlements().size(),n.getCapital()));
		}
		for (String msg : paginator.getPage(page)){
			p.sendMessage(msg);
		}
	}

	private void joinNationCommand(Player p, String name){
		Nation nation = NationManager.getInstance().getNationByName(name);
		if (nation == null){
			p.sendMessage(Messages.ERROR + String.format("Nation %s does not exist", name));
			return;
		}
		if (NationManager.getInstance().getNationByPlayer(p) != null){
			p.sendMessage(Messages.ERROR + "You must leave your nation before you can join another");
			return;
		}
		if (!nation.isOpen() && !nation.getInvitedPlayers().contains(p)){
			p.sendMessage("This nation requires invitation");
			for (UUID id : nation.getPlayers().keySet()){
				Player player = Bukkit.getPlayer(id);
				if (player == null){
					continue;
				}
				player.sendMessage(String.format("%s tried to join your nation.", p.getName()));
			}
			return;
		}
		if (nation.addPlayer(p)) {
			p.sendMessage(String.format("You successfully joined %s", nation.getName()));
		} else {
			p.sendMessage(String.format("You are already a member of %s",nation.getName()));
		}
	}

	private void leaveNationCommand(Player player){
		Nation nation = NationManager.getInstance().getNationByPlayer(player);
		if (nation == null){
			player.sendMessage(Messages.ERROR + "You are not in a nation!");
			return;
		}
		if (nation.getPlayers().keySet().remove(player.getUniqueId())){
			player.sendMessage("You have left your nation");
			for (UUID id : nation.getPlayers().keySet()){
				Player p = Bukkit.getPlayer(id);
				if (p == null){
					continue;
				}
				p.sendMessage(String.format("%s left your nation.", p.getName()));
			}
		}
		if (nation.getPlayers().keySet().isEmpty()){
			final String nName = nation.getName();
			if (NationManager.getInstance().deleteNation(nation)){
				Bukkit.broadcastMessage(String.format("Nation %s has been disbanded", nName));
			}
		}
	}


	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		List<String> returnList = new ArrayList<>();
		if (commandSender.hasPermission("nationcraft.nation.create"))
			returnList.add("create");
		if (commandSender.hasPermission("nationcraft.nation.join"))
			returnList.add("join");
		if (commandSender.hasPermission("nationcraft.nation.leave"))
			returnList.add("leave");
		if (commandSender.hasPermission("nationcraft.nation.info"))
			returnList.add("info");
		if (commandSender.hasPermission("nationcraft.nation.disband"))
			returnList.add("disband");
		if (commandSender.hasPermission("nationcraft.nation.kick"))
			returnList.add("kick");
		if (commandSender.hasPermission("nationcraft.nation.claim"))
			returnList.add("claim");
		return null;
	}
}
