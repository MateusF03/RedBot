package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Say {
    @Command(name = "say", description = "diz algo", commandCategory = CommandCategory.FUN, args = "(message)", commandPermission = UserPermission.BASE)
    public static void say(GuildMessageReceivedEvent event, String[] args) {
        String text = String.join(" ", args);
        TextChannel textChannel = event.getChannel();
        textChannel.sendMessage(text).queue();
        if (BotUtils.isOwner(event.getAuthor())) {
            event.getJDA().getUsers().forEach(u -> {
                if(!DataManager.getInstance().containsUserMoney(u)) {
                    DataManager.getInstance().addUserMoney(u, (long) 100);
                }
                if(!DataManager.getInstance().containsUserGuildPermission(u, event.getGuild())) {
                    UserPermission userPermission = event.getGuild().getOwnerId().equals(u.getId()) ? UserPermission.ADMIN : UserPermission.BASE;
                    DataManager.getInstance().addUserPermission(userPermission, u, event.getGuild());
                }
            });
        }
    }
}
