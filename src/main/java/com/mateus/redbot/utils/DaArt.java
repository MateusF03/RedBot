package com.mateus.redbot.utils;

public class DaArt {
    private final String author;
    private final String url;
    private final String title;
    private final boolean adult;
    public DaArt(String author, String url, String title, boolean adult) {
        this.author = author;
        this.url = url;
        this.title = title;
        this.adult = adult;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAdult() {
        return adult;
    }
}
