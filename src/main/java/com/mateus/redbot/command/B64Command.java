package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.command.SubCommand;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Base64;

public class B64Command {
    @Command(name = "b64", description = "Criptografa algo em base64", commandCategory = CommandCategory.INFO, args = "null", commandPermission = UserPermission.ADMIN)
    public static void b64(GuildMessageReceivedEvent event, String[] args) {
    }
    @SubCommand(name = "encode", description = "Torna uma string em base64", args = "(texto)", permission = UserPermission.BASE)
    public static void encode(GuildMessageReceivedEvent event, String[] args) {
        String text = String.join(" ", args);
        String result = Base64.getEncoder().encodeToString(text.getBytes());
        event.getChannel().sendMessage("***Resultado:*** `" + result + "`").queue();

    }
    @SubCommand(name = "decode", description = "Torna um texto em base64 em um texto comum", args = "(texto)", permission = UserPermission.BASE)
    public static void decode(GuildMessageReceivedEvent event, String[] args) {
        String text = String.join(" ", args);
        try {
            byte[] bytes = Base64.getDecoder().decode(text);
            String result = new String(bytes);
            event.getChannel().sendMessage("***Resultado:*** `" + result + "`").queue();
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("***Não foi possivel decodificar este texto, ele esta em base64?***").queue();
        }
    }
}
