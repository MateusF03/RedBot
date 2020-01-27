package com.mateus.redbot.core.games.core;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Game  {
    int minPlayers();
    int maxPlayers();
    void setPlayers(User[] players);
    void init(GuildMessageReceivedEvent event);
}
