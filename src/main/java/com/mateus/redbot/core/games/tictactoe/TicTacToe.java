package com.mateus.redbot.core.games.tictactoe;

import com.mateus.redbot.core.games.core.Game;
import com.mateus.redbot.core.games.core.GameManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class TicTacToe implements Game, EventListener {
    private final int[][] WIN_CONDITIONS = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };
    private Tile[] board;
    private User[] players;
    private User winner, turn;
    private String message;
    private GuildMessageReceivedEvent event;

    public TicTacToe(GuildMessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public int minPlayers() {
        return 2;
    }

    @Override
    public int maxPlayers() {
        return 2;
    }

    @Override
    public void setPlayers(User[] players) {
        this.players = players;
    }

    @Override
    public void init(GuildMessageReceivedEvent event) {
        board = new Tile[9];
        for (int i = 0; i < board.length; i++) {
            board[i] = new Tile();
        }
        message = ":one: :two: :three: \n:four: :five: :six: \n:seven: :eight: :nine:\n";
        newTurn(event);
    }

    private void newTurn(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(message).queue();
        if (turn == null) {
            turn = players[0];
        } else {
            turn = turn.getIdLong() == players[0].getIdLong() ? players[1] : players[0];
        }
        event.getChannel().sendMessage("**" + turn.getAsMention() + " é a sua vez!**").queue();
        event.getJDA().addEventListener(this);
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent guildEvent = (GuildMessageReceivedEvent) event;
            if (!guildEvent.getChannel().getId().equals(this.event.getChannel().getId())) return;
            if (Arrays.stream(players).noneMatch(u -> u.getId().equals(guildEvent.getAuthor().getId()))) return;
            if (!turn.getId().equals(guildEvent.getAuthor().getId())) {
                guildEvent.getChannel().sendMessage("**Nâo é seu turno!**").queue();
            } else if (!StringUtils.isNumeric(guildEvent.getMessage().getContentRaw())) {
                guildEvent.getChannel().sendMessage("**Envie um número!**").queue();
            } else {
                int chosenNumber = Integer.valueOf(guildEvent.getMessage().getContentRaw());
                if (chosenNumber > 9 || chosenNumber < 1) {
                    guildEvent.getChannel().sendMessage("***O número tem que ser entre 1 e 9***").queue();
                } else {
                    if (!board[chosenNumber - 1].isFree()) {
                        guildEvent.getChannel().sendMessage("***Você não pode selecionar um número já marcado***").queue();
                    } else {
                        board[chosenNumber - 1].setMarker(turn);
                        String[] replaceableArray = {":one:", ":two:",":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:"};
                        String mark = turn.getIdLong() == players[0].getIdLong() ? ":regional_indicator_x:" : ":regional_indicator_o:";
                        message = message.replace(replaceableArray[chosenNumber - 1], mark);
                        if (isOver()) {
                            guildEvent.getChannel().sendMessage(message).queue();
                            if (winner != null) {
                                guildEvent.getChannel().sendMessage("**O vencedor dessa partida é o " + winner.getAsMention() + "!**").queue();
                            } else {
                                guildEvent.getChannel().sendMessage("**Deu velha...***").queue();
                            }
                            end();
                        } else {
                            event.getJDA().removeEventListener(this);
                            newTurn(this.event);
                        }
                    }
                }
            }
        }
    }

    private void end() {
        GameManager.get().getGameSessions().remove(event.getChannel().getIdLong());
        event.getJDA().removeEventListener(this);
    }

    private boolean isOver() {
        for (int[] condition : WIN_CONDITIONS) {
            if (board[condition[0]].isFree() || board[condition[1]].isFree() || board[condition[2]].isFree()) continue;
            if (board[condition[0]].getMarker().getIdLong() == board[condition[1]].getMarker().getIdLong()
                    && board[condition[1]].getMarker().getIdLong() == board[condition[2]].getMarker().getIdLong()) {
                winner = board[condition[1]].getMarker();
                return true;
            }
        }
        for (Tile tile : board) {
            if (tile.isFree()) {
                return false;
            }
        }
        return true;
    }
}
