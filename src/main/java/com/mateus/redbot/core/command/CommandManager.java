package com.mateus.redbot.core.command;

import com.mateus.redbot.core.config.ConfigManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager {
    private static CommandManager instance;
    private List<CommandObject> commandObjects = new ArrayList<>();
    private CommandManager() {}
    public static CommandManager getInstance() {
        if (instance == null) {
            synchronized (CommandManager.class) {
                if (instance == null) {
                    instance = new CommandManager();
                }
            }
        }
        return instance;
    }
    public void registerCommand(Class command) {
        Method[] methods = command.getMethods();
        Method commandMethod = null;
        for (Method method: methods) {
            if (method.getAnnotation(Command.class) != null) {
                commandMethod = method;
            }
        }
        Command c = commandMethod.getAnnotation(Command.class);
        commandObjects.add(new CommandObject(c.name(), c.description(), c.commandCategory(), c.args(), commandMethod));
    }
    public List<CommandObject> getCommandObjects() {
        return commandObjects;
    }
    public void notifyCommands(GuildMessageReceivedEvent event) {
        if (!event.getChannel().canTalk() || event.getAuthor().isBot()) {
            return;
        }
        String content = event.getMessage().getContentRaw();
        String prefix = (String) ConfigManager.getInstance().getConfig().get("prefix");
        if (!content.startsWith(prefix)) return;
        content = content.substring(prefix.length());
        String[] args = content.split("\\s+");
        for (CommandObject commandObject: getCommandObjects()) {
            if (args[0].equalsIgnoreCase(commandObject.getName())) {
                args = Arrays.copyOfRange(args, 1, args.length);
                List<Boolean> arguments = Arrays.stream(commandObject.getArgs()
                .replaceAll("[^()\\[\\]]", "")
                .replace("()", "true\n")
                .replace("[]", "false\n")
                .split("\n")).map(Boolean::valueOf).collect(Collectors.toList());
                for (int i = 0; i<arguments.size(); i++) {
                    if (arguments.get(i) && args.length < i + 1) {
                        event.getChannel().sendMessage("**Erro, argumentos invalidos**\n *O uso correto é:* `" + prefix + commandObject.getName() + " " + commandObject.getArgs()).queue();
                        return;
                    }
                }
                try {
                    commandObject.getCommand().invoke(null, event, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
