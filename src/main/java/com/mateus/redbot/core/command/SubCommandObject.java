package com.mateus.redbot.core.command;

import com.mateus.redbot.core.permissions.UserPermission;

import java.lang.reflect.Method;

public class SubCommandObject {
    private final String name;
    private final String description;
    private final String args;
    private final UserPermission userPermission;
    private final Method method;
    public SubCommandObject(String name, String description, String args, UserPermission userPermission, Method method) {
        this.name = name;
        this.description = description;
        this.args = args;
        this.userPermission = userPermission;
        this.method = method;
    }

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }
}
