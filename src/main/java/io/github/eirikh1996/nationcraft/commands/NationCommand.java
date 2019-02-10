package io.github.eirikh1996.nationcraft.commands;

import java.util.*;

import io.github.eirikh1996.nationcraft.claiming.Shape;
import io.github.eirikh1996.nationcraft.commands.subcommands.nation.*;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.exception.InvalidShapeException;
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

	@Override
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
		NationSubCommand subCommand = null;
		if (args[0].equalsIgnoreCase("create")) {
			if (args.length < 2) {
				sender.sendMessage(Messages.ERROR + "You must specify a name!");
				return true;
			}
			subCommand = new CreateNationSubCommand((Player) sender, args[1]);
		} else if (args[0].equalsIgnoreCase("ally")) {
			if (args[1].length() < 1) {
				sender.sendMessage("You need to specify a nation");
				return true;
			}
			subCommand = new AllyNationSubCommand((Player) sender, args[1]);
		} else if (args[0].equalsIgnoreCase("neutral")) {
			if (args[1].length() < 1) {
				sender.sendMessage("You need to specify a nation");
				return true;
			}
			subCommand = new NeutralNationSubCommand((Player) sender, args[1]);
		} else if (args[0].equalsIgnoreCase("war")) {
			subCommand = null;
		} else if (args[0].equalsIgnoreCase("disband")) {
			subCommand = null;
		}
		//leave
		else if (args[0].equalsIgnoreCase("info")) {
			if (args.length < 2){
				subCommand = new InfoNationSubCommand((Player) sender);
			} else {
				subCommand = new InfoNationSubCommand((Player) sender,args[1]);
			}
		}
		//join
		else if (args[0].equalsIgnoreCase("join")) {
			if (!sender.hasPermission("nationcraft.nation.join")) {
				sender.sendMessage(Messages.ERROR + Messages.NO_PERMISSION);
			}
			if (args.length == 1) {
				sender.sendMessage(Messages.ERROR + "You must specify the nation you wish to join");
			} else {
				subCommand = new JoinNationSubCommand((Player) sender, args[1]);
			}

		} else if (args[0].equalsIgnoreCase("leave")) {
			if (!sender.hasPermission("nationcraft.nation.leave")) {
				sender.sendMessage(Messages.ERROR + Messages.NO_PERMISSION);
				return true;
			}
			subCommand = new LeaveNationSubCommand((Player) sender);

		} else if (args[0].equalsIgnoreCase("list")) {
			if (args.length == 1) {
				subCommand = new ListNationSubCommand((Player) sender);
			} else
				subCommand = new ListNationSubCommand((Player) sender, Integer.parseInt(args[1]));
		} else if (args[0].equalsIgnoreCase("invite")) {
			if (args.length == 1) {
				sender.sendMessage(Messages.ERROR + "You must specify a player name!");
				return true;
			}
			subCommand = new InvitePlayerNationSubCommand((Player) sender, args[1]);
		} else if (args[0].equalsIgnoreCase("claim")) {
			int radius;
			String name;
			Shape shape;
			try {
				radius = Integer.parseInt(args[2]);
			} catch (ArrayIndexOutOfBoundsException e) {
				radius = 0;
			} catch (NumberFormatException e){
				sender.sendMessage(Messages.ERROR + args[2] + " is not a number! Proper usage: /nation claim [circle:square:line:single] [radius] [nation:you]");
				return false;
			}
			try {
				name = args[3];
			} catch (ArrayIndexOutOfBoundsException e) {
				name = "";
			}
			try {
				shape = Shape.getShape(args[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				shape = null;
			} catch (InvalidShapeException e){
				sender.sendMessage(Messages.ERROR + e.getMessage());
				return true;
			}
			subCommand = new ClaimTerritoryNationSubCommand((Player) sender, shape, radius, name);
		} else if (args[0].equalsIgnoreCase("unclaim")) {
			int radius;
			String name;
			Shape shape;
			try {

				radius = Integer.parseInt(args[2]);
			} catch (ArrayIndexOutOfBoundsException e) {
				radius = 0;
			} catch (NumberFormatException e){
				sender.sendMessage(Messages.ERROR + args[2] + " is not a number! Proper usage: /nation claim [circle:square:line:single] [radius] [nation:you]");
				return false;
			}
			try {
				name = args[3];
			} catch (ArrayIndexOutOfBoundsException e) {
				name = "";
			}
			try {
				shape = Shape.getShape(args[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				shape = null;
			} catch (InvalidShapeException e){
				sender.sendMessage(Messages.ERROR + e.getMessage());
				return true;
			}
			subCommand = new UnclaimTerritoryNationSubCommand((Player) sender,shape,radius,name);
		} else if (args[0].equalsIgnoreCase("help")){
		    int page;
		    try {
		        page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
		        sender.sendMessage(Messages.ERROR + args[1] + " is not a valid number!");
		        return true;
            } catch (ArrayIndexOutOfBoundsException e){
		        page = 0;
            }
		    subCommand = new HelpNationSubCommand((Player) sender,page);
        }
		if (subCommand == null){
			throw new NoSuchSubCommandException(args[0] + " is not a valid sub command of command /nation");
		}
		subCommand.execute();
		return true;
	}

	//create








	final String[] subCmds = {"invite","claim","disband"};
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		List<String> completions = new ArrayList<>();
		List<String> returnValues = new ArrayList<>();
		String[] subCmdArr = new String[]{"invite","claim","unclaim"};
		List<String> subCmds = Arrays.asList(subCmdArr);
		NationCraft.getInstance().getLogger().info("Length: " + strings.length);
		if (strings.length == 1 ) {
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
			if (commandSender.hasPermission("nationcraft.nation.list"))
				completions.add("list");
			if (commandSender.hasPermission("nationcraft.nation.unclaim"))
				completions.add("unclaim");
			if (commandSender.hasPermission("nationcraft.nation.help"))
			    completions.add("help");

		} else if (strings[0].equalsIgnoreCase("invite")) {
			//first, fetch names of players currently online
			for (Player p : Bukkit.getOnlinePlayers()) {
				completions.add(p.getName());
			}
			//then add the names of players that have joined in the past
			for (Map<String, Object> dataMap : PlayerManager.getInstance().getPlayers().values()) {
				for (String key : dataMap.keySet()) {
					String name = (String) dataMap.get("name");
					if (!completions.contains(name)) {
						completions.add(name);
					}
				}
			}
		} else if (strings[0].equalsIgnoreCase("claim") || strings[0].equalsIgnoreCase("unclaim")){
			for (String str : Shape.getShapeNames()){
				completions.add(str);
			}
		} else {
			return Collections.emptyList();

		}
		Collections.sort(completions);
		for (String completion : completions) {
			if (completion.startsWith(strings[strings.length - 1].toLowerCase())) {
				returnValues.add(completion);
			}
		}
		return returnValues;
	}

	private class NoSuchSubCommandException extends RuntimeException {
		public NoSuchSubCommandException(String s){
			super(s);
		}
	}
}