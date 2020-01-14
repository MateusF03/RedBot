package com.mateus.redbot.utils;

import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class BotUtils {
    public static boolean isOwner(User user) {
        return user.getId().equals(ConfigManager.getInstance().getConfig().get("ownerID"));
    }
    public static boolean hasGuildPermission(User user, UserPermission permission, Guild guild) {
        UserPermission userPermission = DataManager.getInstance().getUserGuildPermission(user, guild);
        int requiredValue = permission.getValue();
        int userValue = userPermission.getValue();
        return requiredValue <= userValue;
    }
}

