package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TerritoryNationSubCommand extends NationSubCommand {
    private final Action action;
    public TerritoryNationSubCommand(Player sender, Action action) {
        super(sender);
        this.action = action;
    }

    @Override
    public void execute() {

    }

    public enum Action {
        SELECT, DESELECT, SET_ACCESS;
        private static HashMap<String, Action> BY_NAME = new HashMap<>();
        static {
            for (Action a : Action.values()){
                BY_NAME.put(a.name().toLowerCase().replace("_",""),a);
            }
        }
        public Action getAtion(String aName){
            Action ret = BY_NAME.get(aName.toLowerCase());
            if (ret == null){
                throw new NoSuchActionException("Invalid action: " + aName);
            }
            return ret;
        }

        private class NoSuchActionException extends RuntimeException{
            public NoSuchActionException(String s){
                super(s);
            }
        }
    }
}
