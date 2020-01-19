package com.mateus.redbot.core.data;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DailyManager {
    private static DailyManager instance;
    private JSONObject jsonObject;
    private File file = new File(System.getProperty("user.dir"), "daily.json");
    private DailyManager() { }
    public static DailyManager get() {
        if (instance == null) {
            synchronized (DailyManager.class) {
                if (instance == null) {
                    instance = new DailyManager();
                }
            }
        }
        return instance;
    }
    public void setup() {
        JDALogger.getLog("RedBot").info("Carregando dailys...");
        if (!file.exists()) {
            try {
                file.createNewFile();
                this.jsonObject = new JSONObject();
                jsonObject.put("dailys", null);
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try(FileReader fileReader = new FileReader(file)) {
                this.jsonObject = (JSONObject) new JSONParser().parse(fileReader);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public void addDaily(User user) {
        if (this.jsonObject.containsValue(user.getId())) {
            JDALogger.getLog("RedBot").info("Este usuário já esta no arquivo");
            return;
        }
        long daily = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        this.jsonObject.put(user.getId(), daily);
        save();
    }
    private void save() {
        try(FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(this.jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkIfDailyPassed(User user) {
        long daily = this.jsonObject.get(user.getId()) != null ? (long) this.jsonObject.get(user.getId()) : 0;
        if (daily == 0) return true;
        return System.currentTimeMillis() > daily;
    }
}
