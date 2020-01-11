package com.mateus.redbot.core.command;

import java.lang.reflect.Method;

public class CommandObject {
    private final String name;
    private final String description;
    private final CommandCategory commandCategory;
    private final String args;
    private final Method command;

    public CommandObject(String name, String description, CommandCategory commandCategory, String args, Method command) {
        this.name = name;
        this.description = description;
        this.commandCategory = commandCategory;
        this.args = args;
        this.command = command;
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
}
