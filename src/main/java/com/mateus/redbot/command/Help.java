package com.mateus.redbot.command;

import com.mateus.redbot.core.command.*;
import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Help {
    @Command(name = "help", description = "comando de help", commandCategory = CommandCategory.INFO, args = "[comando]", commandPermission = UserPermission.BASE)
    public static void help(GuildMessageReceivedEvent event, String[] args) {
        if (args.length >= 1) {
            if (BotUtils.getCommandByName(args[0]) == null) {
                event.getChannel().sendMessage("**Comando desconhecido!**").queue();
            } else {
                CommandObject command = BotUtils.getCommandByName(args[0]);
                String description = "";
                if (command.getDescription().length() > 0 || command.getDescription() != null) {
                    description = "**" + command.getDescription() + "**\n";
                }
                Field field = null;
                if(!command.getArgs().equals("null")){
                    String perm = "**Permissão: **" + command.getUserPermission().toString();
                    field = new Field((ConfigManager.getInstance().getPrefix() + command.getName() + " " + command.getArgs()).replace("null", ""), description + perm, false);
                }
                String description2 = "";
                if (field == null && description.length() > 0) {
                    description2 = description;
                }
                List<Field> subfields = new ArrayList<>();
                if (command.getSubCommands() != null) {
                    for (SubCommandObject s: command.getSubCommands()) {
                        String subdesc = "";
                        if (s.getDescription().length() > 0) {
                            subdesc = "    **" + s.getDescription() + "**\n";
                        }
                        subfields.add(new Field(ConfigManager.getInstance().getPrefix() + command.getName() + " " + s.getName() + " " + s.getArgs(), subdesc + "**Permissão: **" + s.getUserPermission().toString(), false));
                    }
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Informação do comando:");
                embedBuilder.setFooter("Pedido por: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
                embedBuilder.setDescription(description2 + "**Categoria: **" +  command.getCommandCategory().toString());
                embedBuilder.setColor(Color.RED);
                embedBuilder.addField(field);
                for (Field f: subfields) {
                    embedBuilder.addField(f);
                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        } else {
            HashMap<String, List<CommandObject>> helpMap = new HashMap<>();
            List<String> cmdNames = CommandManager.getInstance().getCommandObjects().stream().map(CommandObject::getName).collect(Collectors.toList());
            for (String i: cmdNames) {
                if (!BotUtils.getCommandByName(i).getCommandCategory().toString().equals("")) {
                    helpMap.computeIfAbsent(BotUtils.getCommandByName(i).getCommandCategory().toString(), k -> new ArrayList<>());
                    helpMap.get(BotUtils.getCommandByName(i).getCommandCategory().toString()).add(BotUtils.getCommandByName(i));
                }
            }
            System.out.println(cmdNames.size());
            String description = "";
            for (String i : helpMap.keySet()) {
                description = description + "\n**" + i + "**: ";
                for (CommandObject c : helpMap.get(i)) {
                    description = description + "`" + c.getName() + "`, ";
                }
                description = description.substring(0, description.length() - 2);
            }
            description = description+String.format("\n\nPara informação de um comando especifico, use %shelp (comando)", ConfigManager.getInstance().getPrefix());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Help", null, event.getJDA().getSelfUser().getAvatarUrl());
            embedBuilder.setDescription(description);
            embedBuilder.setFooter("Pedido por: " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setColor(Color.RED);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
