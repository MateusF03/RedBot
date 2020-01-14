package com.mateus.redbot.core.config;


import net.dv8tion.jda.internal.utils.JDALogger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static ConfigManager instance;
    private ConfigManager() {}
    private File file = new File(System.getProperty("user.dir"), "config.json");
    private JSONObject configJson;
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                instance = new ConfigManager();
            }
        }
        return instance;
    }
    public void setup() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                this.configJson = new JSONObject();
                this.configJson.put("prefix", "r!");
                this.configJson.put("status", "r!help");
                this.configJson.put("streaming", false);
                this.configJson.put("token", "none");
                this.configJson.put("ownerID", "none");
                this.configJson.put("imgflip-username", "none");
                this.configJson.put("imgflip-password", "none");
                this.configJson.put("mongodb-password", "none");
                this.configJson.put("mongodb-username", "none");
                fileWriter.write(this.configJson.toJSONString());
                fileWriter.flush();
                JDALogger.getLog("RedBot").info("Criando o template da config, o bot agora vai desligar");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try(FileReader fileReader = new FileReader(file)) {
                JSONParser jsonParser = new JSONParser();
                this.configJson = (JSONObject) jsonParser.parse(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public JSONObject getConfig() {
        return configJson;
    }
}
