package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.data.DailyManager;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DailyCommand {
    @Command(name = "daily", description = "Pega uma quantia de dinheiro diaria", commandCategory = CommandCategory.FUN, args = "null", commandPermission = UserPermission.BASE)
    public static void daily(GuildMessageReceivedEvent event, String[] args) {
        DailyManager dailyManager = DailyManager.get();
        if (!dailyManager.checkIfDailyPassed(event.getAuthor())) {
            event.getChannel().sendMessage("**Você ainda não pode pegar o daily!").queue();
        } else {
            DataManager dataManager = DataManager.getInstance();
            dataManager.updateUserMoney(event.getAuthor(), dataManager.getUserMoney(event.getAuthor()) + 100 );
            event.getChannel().sendMessage("Foram adicionados 100 dinheiros na sua conta").queue();
            dailyManager.addDaily(event.getAuthor());
        }
    }
}
