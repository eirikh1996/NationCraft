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
		permission = "nationcraft.command.nation";
	}
	private final TextComponent PERMISSION_MESSAGE = NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NO_PERMISSION);

	@Override
	protected boolean validateCommandSender(NCCommandSender sender) {
		if (!(sender instanceof NCPlayer)) {
			sender.sendMessage("Error: You must be a player to execute this command!");
			return false;
		}
		return true;
	}

	@Override
	public void execute(NCCommandSender sender) {

		sender.sendMessage("Type /nation help for help on the nation command");
	}
}