package io.github.eirikh1996.nationcraft.core.commands;

import java.util.*;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.nation.*;
import net.kyori.adventure.text.TextComponent;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;


public class NationCommand extends Command {

	public NationCommand() {
		super("nation", Arrays.asList("n"));
		addChild(new NationAllyCommand());
		addChild(new NationClaimCommand());
		addChild(new NationCreateCommand());
		addChild(new NationFlagCommand());
		addChild(new NationHelpCommand());
		addChild(new NationHomeCommand());
		addChild(new NationInfoCommand());
		addChild(new NationInviteCommand());
		addChild(new NationJoinCommand());
		addChild(new NationKickCommand());
		addChild(new NationLeaveCommand());
		addChild(new NationListCommand());
		addChild(new NationNeutralCommand());
		addChild(new NationTruceCommand());
		addChild(new NationUnclaimCommand());
		addChild(new NationWarCommand());
	}
	private final TextComponent PERMISSION_MESSAGE = NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NO_PERMISSION);

	@Override
	public void execute(NCCommandSender sender, String[] args) {
		if (!(sender instanceof NCPlayer)) {
			sender.sendMessage("Error: You must be a player to execute this command!");
			return;
		}
		if (args.length < 1) {
			sender.sendMessage("Type /nation help for help on the nation command");
			return;
		}
		if (!children.containsKey(args[0])) {
			sender.sendMessage("Invalid sub-command: " + args[0]);
			return;
		}
		children.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	private class NoSuchSubCommandException extends RuntimeException {
		public NoSuchSubCommandException(String s){
			super(s);
		}
	}
}