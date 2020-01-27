package com.mateus.redbot.core.command;

import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.utils.BotUtils;
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
        List<Method> subCommands = new ArrayList<>();
        for (Method method: methods) {
            if (method.getAnnotation(Command.class) != null) {
                commandMethod = method;
            } else if (method.getAnnotation(SubCommand.class) != null) {
                subCommands.add(method);
            }
        }
        List<SubCommandObject> subCommandObjects = new ArrayList<>();
        if (!subCommands.isEmpty()) {
            for (Method m: subCommands) {
                SubCommand subCommand = m.getAnnotation(SubCommand.class);
                subCommandObjects.add(new SubCommandObject(subCommand.name(), subCommand.description(),subCommand.args(), subCommand.permission(), m));
            }
        }
        Command c = commandMethod.getAnnotation(Command.class);
        if (!subCommandObjects.isEmpty()) {
            commandObjects.add(new CommandObject(c.name(), c.description(), c.commandCategory(), c.args(),c.commandPermission(), commandMethod, subCommandObjects));
        } else {
            commandObjects.add(new CommandObject(c.name(), c.description(), c.commandCategory(), c.args(),c.commandPermission(), commandMethod, null));
        }
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
                if (commandObject.getSubCommands() != null && args.length > 0) {
                    List<String> subNames = commandObject.getSubCommands().stream().map(SubCommandObject::getName).collect(Collectors.toList());
                    final String subName = args[0];
                    if (subNames.contains(subName)) {
                        SubCommandObject subCommand = commandObject.getSubCommands().stream().filter(s -> s.getName().equals(subName)).collect(Collectors.toList()).get(0);
                        args = Arrays.copyOfRange(args, 1, args.length);
                        List<Boolean> arguments = Arrays.stream(subCommand.getArgs()
                                .replaceAll("[^()\\[\\]]", "")
                                .replace("()", "true\n")
                                .replace("[]", "false\n")
                                .split("\n")).map(Boolean::valueOf).collect(Collectors.toList());
                        for (int i = 0; i<arguments.size(); i++) {
                            if (arguments.get(i) && args.length < i + 1) {
                                event.getChannel().sendMessage("**Erro, argumentos invalidos**\n *O uso correto é:* `" + prefix + commandObject.getName() + " " + subCommand.getName() + " " + subCommand.getArgs() + "`").queue();
                                return;
                            }
                        }
                        if (!BotUtils.hasGuildPermission(event.getAuthor(), subCommand.getUserPermission(), event.getGuild())){
                            event.getChannel().sendMessage("**Você não tem permissão para executar este comando**").queue();
                            return;
                        }
                        try {
                            subCommand.getMethod().invoke(null, event, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                List<Boolean> arguments = Arrays.stream(commandObject.getArgs()
                .replaceAll("[^()\\[\\]]", "")
                .replace("()", "true\n")
                .replace("[]", "false\n")
                .split("\n")).map(Boolean::valueOf).collect(Collectors.toList());
                for (int i = 0; i<arguments.size(); i++) {
                    if (arguments.get(i) && args.length < i + 1) {
                        event.getChannel().sendMessage("**Erro, argumentos invalidos**\n *O uso correto é:* `" + prefix + commandObject.getName() + " " + commandObject.getArgs() + "`").queue();
                        return;
                    }
                }
                if (!BotUtils.hasGuildPermission(event.getAuthor(), commandObject.getUserPermission(), event.getGuild())) {
                    event.getChannel().sendMessage("**Você não tem permissão para executar este comando**").queue();
                    return;
                }
                try {
                    commandObject.getCommand().invoke(null, event, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
