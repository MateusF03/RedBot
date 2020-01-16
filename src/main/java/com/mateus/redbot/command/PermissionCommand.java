package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.BotUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PermissionCommand {

    @Command(name = "perm", description = "Muda a permissão de um usuário", commandCategory = CommandCategory.GUILD, args = "(usuário) (permissão)", commandPermission = UserPermission.BASE)
    public static void perm(GuildMessageReceivedEvent event, String[] args) {
        DataManager dataManager = DataManager.getInstance();
        if (BotUtils.hasGuildPermission(event.getAuthor(), UserPermission.ADMIN, event.getGuild()) || event.getGuild().getOwnerIdLong() == event.getAuthor().getIdLong()) {
            String userArg = args[0];
            User user = null;
            long id = 0;
            try {
                id = Long.valueOf(userArg);
            } catch (NumberFormatException e) {
                System.out.println("Não é um número");
            }
            if (id != 0) {
                user = event.getJDA().getUserById(id);
            } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
                user = event.getMessage().getMentionedUsers().get(0);
            }
            if (user == null) {
                event.getChannel().sendMessage("Não foi possivel achar este usuário!").queue();
                return;
            }
            try {
                UserPermission userPermission = UserPermission.valueOf(args[1]);
                dataManager.updateUserPermission(userPermission, user, event.getGuild());
                event.getChannel().sendMessage(user.getName() + " agora tem a permissão de " + userPermission.toString()).queue();
            } catch (IllegalArgumentException e) {
                event.getChannel().sendMessage("**Essa permissão não existe, os tipos de permissões existentes são:").queue();
                for(UserPermission userPermission: UserPermission.values()) {
                    event.getChannel().sendMessage("- `" + userPermission.toString() + "`").queue();
                }
            }
        }
    }
}
