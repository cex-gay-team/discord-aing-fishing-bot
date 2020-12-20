package com.cex.common.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;

@Component
public class DiscordSendUtil {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ServletContext servletContext;

    public void sendMessage(String message, long channelId) {
        JDA discordJda = (JDA) applicationContext.getBean("discordJDA");
        TextChannel textChannel = discordJda.getTextChannelById(channelId);
        textChannel.sendMessage(message).queue();
    }

    public void sendFile(String fileName, long channelId) {
        JDA discordJda = (JDA) applicationContext.getBean("discordJDA");
        TextChannel textChannel = discordJda.getTextChannelById(channelId);
        File file = new File(servletContext.getRealPath("/WEB-INF/images/" + fileName));
        if(file.exists()) {
            textChannel.sendFile(file).queue();
        }
    }

    public void moveUser(long channelId, long userId) {
        JDA discordJda = (JDA) applicationContext.getBean("discordJDA");
        User user = discordJda.getUserById(userId);
        TextChannel textChannel = discordJda.getTextChannelById(channelId);
    }
}
