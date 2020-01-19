package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class RateCommand {
    @Command(name = "rate", description = "Avalia algo", commandCategory = CommandCategory.FUN, args = "(algo para avaliar)", commandPermission = UserPermission.BASE)
    public static void rate(GuildMessageReceivedEvent event, String[] args) {
        String text = String.join(" ", args);
        int value = new Random().nextInt(10) + 1;
        event.getChannel().sendMessage("\uD83E\uDD14 Eu avalio " + text + " um " + value + "/10").queue();
    }
}
