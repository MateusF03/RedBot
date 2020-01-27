package com.mateus.redbot;

import com.mateus.redbot.command.*;
import com.mateus.redbot.core.command.CommandManager;
import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.data.DailyManager;
import com.mateus.redbot.core.data.DataManager;
import com.mateus.redbot.listener.MessageListener;
import com.mateus.redbot.utils.UserInputScheduler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDA jda;
    public static void main(String[] args) {
        DataManager dataManager = DataManager.getInstance();
        Logger logger = JDALogger.getLog("RedBot");
        logger.info("O Bot está iniciando!");
        ConfigManager configManager = ConfigManager.getInstance();
        logger.info("Iniciando config...");
        configManager.setup();
        dataManager.setup();
        DailyManager.get().setup();
        try {
            JDABuilder jdaBuilder = new JDABuilder((String) configManager.getConfig().get("token"));
            registerCommands(CommandManager.getInstance());
            String status = (String) configManager.getConfig().get("status");
            if ((boolean) configManager.getConfig().get("streaming")) {
                jdaBuilder.setActivity(Activity.streaming(status, null));
            } else jdaBuilder.setActivity(Activity.playing(status));
            jda = jdaBuilder.build();
            jda.addEventListener(new MessageListener());
            jda.addEventListener(new UserInputScheduler());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    private static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(Say.class);
        commandManager.registerCommand(MemeGenerator.class);
        commandManager.registerCommand(PermissionCommand.class);
        commandManager.registerCommand(Help.class);
        commandManager.registerCommand(DeviantArtCommand.class);
        commandManager.registerCommand(GuildInfo.class);
        commandManager.registerCommand(UserInfo.class);
        commandManager.registerCommand(DailyCommand.class);
        commandManager.registerCommand(RateCommand.class);
        commandManager.registerCommand(SamCommand.class);
        commandManager.registerCommand(AvatarCommand.class);
        commandManager.registerCommand(GataCommand.class);
        commandManager.registerCommand(Ojjo.class);
        commandManager.registerCommand(JpegCommand.class);
        commandManager.registerCommand(KickCommand.class);
        commandManager.registerCommand(TicTacToeCommand.class);
        commandManager.registerCommand(B64Command.class);
    }
}
