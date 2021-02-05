package cn.n3ro.ghostclient.management;

import cn.n3ro.ghostclient.command.Command;
import cn.n3ro.ghostclient.command.commands.CommandBind;
import cn.n3ro.ghostclient.command.commands.CommandConfig;
import cn.n3ro.ghostclient.command.commands.CommandToggle;

import java.util.ArrayList;

public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList<Command>();

    public CommandManager() {
        add(new CommandToggle(new String[] {"toggle", "t"}));
        add(new CommandBind(new String[] {"bind"}));
        add(new CommandConfig(new String[]{"config"}));
    }

    public void add(Command c) {
        CommandManager.commands.add(c);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}
