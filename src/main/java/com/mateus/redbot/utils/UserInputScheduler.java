package com.mateus.redbot.utils;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class UserInputScheduler extends ListenerAdapter {
    public static HashMap<Long, Triple<ScheduleType, List<String>, Consumer<Event>>> schedules = new HashMap<>();
    public enum ScheduleType{
        REACTION, ANSWER
    }
    public static void addScheduler(User user, ScheduleType scheduleType, Consumer<Event> reaction, String... requiredInputs) {
        Triple<ScheduleType, List<String>, Consumer<Event>> triple = new Triple<>(scheduleType, Arrays.asList(requiredInputs), reaction);
        if (schedules.containsKey(user.getIdLong())) return;
        schedules.put(user.getIdLong(), triple);
    }
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (schedules.containsKey(event.getAuthor().getIdLong()) && schedules.get(event.getAuthor().getIdLong()).getValue0() == ScheduleType.ANSWER) {
            if (schedules.get(event.getAuthor().getIdLong()).getValue1().contains(event.getMessage().getContentRaw())) {
                schedules.get(event.getAuthor().getIdLong()).getValue2().accept(event);
                schedules.remove(event.getAuthor().getIdLong());
            }
        }
    }
}
