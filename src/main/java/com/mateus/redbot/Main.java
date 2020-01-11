package com.mateus.redbot;

import com.mateus.redbot.command.MemeGenerator;
import com.mateus.redbot.command.Say;
import com.mateus.redbot.core.command.CommandManager;
import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        Logger logger = JDALogger.getLog("RedBot");
        logger.info("O Bot está iniciando!");
        ConfigManager configManager = ConfigManager.getInstance();
        logger.info("Iniciando config...");
        configManager.setup();
        try {
            JDABuilder jdaBuilder = new JDABuilder((String) configManager.getConfig().get("token"));
            registerCommands(CommandManager.getInstance());
            String status = (String) configManager.getConfig().get("status");
            if ((boolean) configManager.getConfig().get("streaming")) {
                jdaBuilder.setActivity(Activity.streaming(status, null));
            } else jdaBuilder.setActivity(Activity.playing(status));
            JDA jda = jdaBuilder.build();
            jda.addEventListener(new MessageListener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    private static void registerCommands(CommandManager commandManager) {
        commandManager.registerCommand(Say.class);
        commandManager.registerCommand(MemeGenerator.class);
    }
}
