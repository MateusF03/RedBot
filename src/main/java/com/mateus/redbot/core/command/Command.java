package com.mateus.redbot.core.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
    String description();
    CommandCategory commandCategory();
    String args();

}
