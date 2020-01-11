package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Say {
    @Command(name = "say", description = "diz algo", commandCategory = CommandCategory.FUN, args = "(message)")
    public static void say(GuildMessageReceivedEvent event, String[] args) {
        String text = String.join(" ", args);
        TextChannel textChannel = event.getChannel();
        textChannel.sendMessage(text).queue();
    }
}
