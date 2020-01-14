package com.mateus.redbot.core.permissions;

public enum UserPermission {
    BASE(1), GUILD(2), ADMIN(3);
    private int value;
    UserPermission(int i) {
        this.value = i;
    }
    public int getValue() {
        return value;
    }
}
