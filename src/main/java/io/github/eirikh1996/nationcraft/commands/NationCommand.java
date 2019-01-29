package io.github.eirikh1996.nationcraft.commands;

import java.util.*;

import io.github.eirikh1996.nationcraft.claiming.ClaimUtils;
import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerInviteEvent;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
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
			sender.sendMessage("Type /nation help for help on the nation command");
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
		} else if (args[0].equalsIgnoreCase("invite")){
			if (args.length == 1){
				sender.sendMessage(Messages.ERROR + "You must specify a player name!");
				return true;
			}
            invitePlayerCommand((Player) sender, args[1]);
		} else if (args[0].equalsIgnoreCase("claim")) {

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
		Set<Chunk> territory = new HashSet<>();
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
		for (Nation n : NationManager.getInstance()){
			String nationName = NationManager.getInstance().getColor(p,n) + n.getName() + ChatColor.YELLOW;
			int settlements = n.getSettlements() != null ? n.getSettlements().size() : 0;
			paginator.addLine(String.format("%s: Players: %d, Settlements: %d, Capital: %s", nationName,n.getPlayers().keySet().size(),settlements,n.getCapital()));
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
		if (!nation.isOpen() && !nation.getInvitedPlayers().contains(p.getUniqueId())){
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
		if (nation.getPlayers().keySet().size() >= Settings.maxPlayersPerNation){
			p.sendMessage(Messages.ERROR + "Nation " + nation.getName() + " is full! Join another nation, or create your own.");
			return;
		}
		if (nation.addPlayer(p)) {
		    nation.getInvitedPlayers().remove(p.getUniqueId());
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
	private void invitePlayerCommand(Player p, String playerName){
		Nation n = NationManager.getInstance().getNationByPlayer(p);
		if (n == null){
			p.sendMessage(Messages.ERROR + "You are not in a nation!");
			return;
		}
		if (!PlayerManager.getInstance().playerIsAtLeast(p, Ranks.OFFICER)){
			p.sendMessage("You must be at least officer to invite players");
			return;
		}
		UUID id;
		if (Bukkit.getPlayer(playerName) != null){
		    Player target = Bukkit.getPlayer(playerName);
			id = target.getUniqueId();
            target.sendMessage("You have been invited to join " + NationManager.getInstance().getColor(target, n) + n.getName());
		} else {
			id = PlayerManager.getInstance().getPlayerIDFromName(playerName);
		}
		if (id == null){
			p.sendMessage(Messages.ERROR + "Player " + playerName + " has never joined the server!");
			return;
		}
        n.invite(id);
	}

	private void claimTerritoryCommand(Player sender, String shapeName, int radius, String nationName){
		Nation nation = NationManager.getInstance().getNationByPlayer(sender);
		NationManager manager = NationManager.getInstance();
		Shape shape = Shape.getShape(shapeName);
		if (nation == null){
			sender.sendMessage(Messages.ERROR + "You are not in a nation!");
			return;
		}
		if (shape == null){
			Chunk claim = sender.getLocation().getChunk();
			Nation foundNation = manager.getNationAt(claim);
			if (foundNation != null){
				if (foundNation.isStrongEnough()){
					sender.sendMessage(foundNation.getName() + " owns this land and is strong enough to hold it.");
				} else if (foundNation.getName().equalsIgnoreCase("safezone") || foundNation.getName().equalsIgnoreCase("warzone")){
					sender.sendMessage("You cannot claim from " + foundNation.getName() + "'s territory!");
				}
				else {
					sender.sendMessage("You claimed 1 piece of land from " + foundNation.getName());
					for (UUID id : foundNation.getPlayers().keySet()){
						Player p = Bukkit.getPlayer(id);
						if (p == null){
							continue;
						}
						p.sendMessage(nation.getName() + " claimed 1 piece of territory from your land!");
					}
				}
			}
			if (nation.getTerritory().add(claim)){

			}
		} else if (shape == Shape.CIRCLE){
			Set<Chunk> claimedTerritory = ClaimUtils.claimCircularTerritory(sender, radius);
			boolean alreadyOwning = false;
			boolean strongEnough = false;
			for (Chunk c : claimedTerritory){
			    Nation owner = NationManager.getInstance().getNationAt(c);
			    if (owner == nation){
			        alreadyOwning = true;
			        claimedTerritory.remove(c);
                }
			    else if (owner != null && owner != nation){
			        if (owner.isStrongEnough()){
			            strongEnough = true;
			            claimedTerritory.remove(c);
                    }
                }
            }
			if (alreadyOwning){
			    sender.sendMessage("Your nation already owns this land");
            }
			if (strongEnough){
			    sender.sendMessage("");
            }
		}
		if (nationName != null){
			if (!sender.hasPermission("nation.claim.others")){
				sender.sendMessage(Messages.ERROR + "You can only claim territory for your own nation");
				return;
			}
		}
	}


	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		List<String> completions = new ArrayList<>();
		if (strings.length != 1 && !strings[0].equalsIgnoreCase("invite")){
			return Collections.emptyList();
		}else if (strings[0].equalsIgnoreCase("invite")){
			//first, fetch names of players currently online
			for (Player p : Bukkit.getOnlinePlayers()){
				completions.add(p.getName());
			}
			//then add the names of players that have joined in the past
			for (Map<String, Object> dataMap : PlayerManager.getInstance().getPlayers().values()){
				for (String key : dataMap.keySet()){
					String name = (String) dataMap.get("name");
					if (!completions.contains(name)){
						completions.add(name);
					}
				}
			}
		}
		else  {
			if (commandSender.hasPermission("nationcraft.nation.create"))
				completions.add("create");
			if (commandSender.hasPermission("nationcraft.nation.join"))
				completions.add("join");
			if (commandSender.hasPermission("nationcraft.nation.leave"))
				completions.add("leave");
			if (commandSender.hasPermission("nationcraft.nation.info"))
				completions.add("info");
			if (commandSender.hasPermission("nationcraft.nation.disband"))
				completions.add("disband");
			if (commandSender.hasPermission("nationcraft.nation.kick"))
				completions.add("kick");
			if (commandSender.hasPermission("nationcraft.nation.claim"))
				completions.add("claim");
			if (commandSender.hasPermission("nationcraft.nation.invite"))
				completions.add("invite");
		}
		List<String> returnValues = new ArrayList<>();
		for (String completion : completions){
			if (completion.startsWith(strings[strings.length - 1].toLowerCase())){
				returnValues.add(completion);
			}
		}
		return returnValues;
	}
}
