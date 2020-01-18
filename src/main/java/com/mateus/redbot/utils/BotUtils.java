package com.mateus.redbot.utils;

import com.mateus.redbot.core.command.CommandManager;
import com.mateus.redbot.core.command.CommandObject;
import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.stream.Collectors;

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
    public static CommandObject getCommandByName(String name) {
        List<CommandObject> l = CommandManager.getInstance().getCommandObjects().stream().filter(c -> c.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (!l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }
    public static String capitalizeFirst(String text) {
        String result = text.substring(1);
        result = Character.toUpperCase(text.charAt(0)) + result;
        return result;
    }
}

