package com.mateus.redbot.listener;

import com.mateus.redbot.core.command.CommandManager;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (BotUtils.isOwner(event.getAuthor()) && event.getMessage().getContentRaw().equals("shutdown")) {
            event.getJDA().shutdown();
            System.exit(0);
        }
        CommandManager.getInstance().notifyCommands(event);
    }
}
