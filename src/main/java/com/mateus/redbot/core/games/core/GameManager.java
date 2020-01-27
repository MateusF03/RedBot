package com.mateus.redbot.core.games.core;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private HashMap<Long, Game> gameSession = new HashMap<>();
    private List<Game> games = new ArrayList<>();
    private GameManager() { }
    public static GameManager get() {
        if (instance == null) {
            synchronized (GameManager.class) {
                if (instance == null) instance = new GameManager();
            }
        }
        return instance;
    }
    public HashMap<Long, Game> getGameSessions() {
        return gameSession;
    }
    public void addGameSession(GuildMessageReceivedEvent event, Game game, User... users) {
        if (gameSession.containsKey(event.getChannel().getIdLong())) {
            event.getChannel().sendMessage("**Já tem uma jogatina neste canal!**").queue();
            return;
        }
        if (game.minPlayers() > users.length || game.maxPlayers() < users.length) {
            event.getChannel().sendMessage("**Quantidade de jogadores invalidos!**").queue();
            return;
        }
        game.setPlayers(users);
        gameSession.put(event.getChannel().getIdLong(), game);
        game.init(event);
    }
    public void registerGame(Game game) {
        this.games.add(game);
    }
}
