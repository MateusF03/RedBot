package com.mateus.redbot.utils;

import com.mateus.redbot.core.config.ConfigManager;
import net.dv8tion.jda.api.entities.User;

public class BotUtils {
    public static boolean isOwner(User user) {
        return user.getId().equals(ConfigManager.getInstance().getConfig().get("ownerID"));
    }
}
