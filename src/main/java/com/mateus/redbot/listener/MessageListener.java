package com.mateus.redbot.listener;

import com.mateus.redbot.core.command.CommandManager;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!DataManager.getInstance().containsUserMoney(event.getAuthor())) {
            DataManager.getInstance().addUserMoney(event.getAuthor(), (long) 100);
        } else if (!DataManager.getInstance().containsUserGuildPermission(event.getAuthor(), event.getGuild())) {
            DataManager.getInstance().addUserPermission(UserPermission.BASE, event.getAuthor(), event.getGuild());
        }
        if (BotUtils.isOwner(event.getAuthor()) && event.getMessage().getContentRaw().equals("shutdown")) {
            event.getJDA().shutdown();
            System.exit(0);
        }
        CommandManager.getInstance().notifyCommands(event);
    }
}
