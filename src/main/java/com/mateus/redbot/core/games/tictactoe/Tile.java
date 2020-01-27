package com.mateus.redbot.core.games.tictactoe;

import net.dv8tion.jda.api.entities.User;

public class Tile {
    private User marker;
    public Tile() {
        this.marker = null;
    }
    public boolean isFree() {
        return marker == null;
    }
    public void setMarker(User marker) {
        this.marker = marker;
    }
    public User getMarker() {
        return marker;
    }
}
