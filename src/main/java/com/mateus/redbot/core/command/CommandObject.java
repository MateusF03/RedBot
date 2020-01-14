package com.mateus.redbot.core.command;

import com.mateus.redbot.core.permissions.UserPermission;

import java.lang.reflect.Method;
import java.util.List;

public class CommandObject {
    private final String name;
    private final String description;
    private final CommandCategory commandCategory;
    private final String args;
    private final Method command;
    private final UserPermission userPermission;
    private List<SubCommandObject> subCommands;
    public CommandObject(String name, String description, CommandCategory commandCategory, String args, UserPermission userPermission, Method command, List<SubCommandObject> subCommands) {
        this.name = name;
        this.description = description;
        this.commandCategory = commandCategory;
        this.args = args;
        this.command = command;
        this.userPermission = userPermission;
        this.subCommands = subCommands;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandCategory getCommandCategory() {
        return commandCategory;
    }

    public String getArgs() {
        return args;
    }

    public Method getCommand() {
        return command;
    }
    public UserPermission getUserPermission() {
        return userPermission;
    }
    public List<SubCommandObject> getSubCommands() {
        return subCommands;
    }
}
