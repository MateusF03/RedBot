package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.games.core.GameManager;
import com.mateus.redbot.core.games.tictactoe.TicTacToe;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.UserInputScheduler;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

public class TicTacToeCommand {
    @Command(name = "tictactoe", description = "Cria uma partida de jogo da velha", commandCategory = CommandCategory.GAMES, args = "(usuário que vai jogar)", commandPermission = UserPermission.BASE)
    public static void tictactoe(GuildMessageReceivedEvent event, String[] args) {
        User invitedPlayer = null;
        if (StringUtils.isNumeric(args[0])) {
            invitedPlayer = event.getJDA().getUserById(Long.valueOf(args[0]));
        } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
            invitedPlayer = event.getMessage().getMentionedUsers().get(0);
        }
        if (invitedPlayer == null) {
            event.getChannel().sendMessage("**Você não colocou nenhum usuário!**").queue();
            return;
        }
        event.getChannel().sendMessage("**" + invitedPlayer.getAsMention() + " você aceita jogar uma partida de jogo da velha com " + event.getAuthor().getName() + "?**").queue();
        User finalInvitedPlayer = invitedPlayer;
        UserInputScheduler.addScheduler(invitedPlayer, UserInputScheduler.ScheduleType.ANSWER, e -> {
                    GuildMessageReceivedEvent guildEvent = (GuildMessageReceivedEvent) e;
                    if (guildEvent.getMessage().getContentRaw().equalsIgnoreCase("não")) {
                        guildEvent.getChannel().sendMessage("**Desculpe, mas ele te rejeitou..**").queue();
                    } else if(guildEvent.getMessage().getContentRaw().equalsIgnoreCase("sim")) {
                        guildEvent.getChannel().sendMessage("**Ok, iniciarei o jogo!**").queue();
                        GameManager.get().addGameSession(event, new TicTacToe(event), finalInvitedPlayer, event.getAuthor());
                    }
                }, "sim", "não");
    }
}
