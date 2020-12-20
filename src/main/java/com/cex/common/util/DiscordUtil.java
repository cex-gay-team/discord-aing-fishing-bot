package com.cex.common.util;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Objects;

public class DiscordUtil {
    private static final String[] INVALID_CHANNEL_NAMES = {"PRIVATE"};
    public static final String[] COMMAND_NAMES = {"rod", "ping", "bait", "location", "fishing", "buy", "sell", "help", "me"};
    public static final int PREFIX_LENGTH = 1;

    private DiscordUtil() {
    }

    private static class InnerDiscordInitializerClazz {
        private static final DiscordUtil uniqueInstance = new DiscordUtil();
    }

    public static DiscordUtil getInstance() {
        return InnerDiscordInitializerClazz.uniqueInstance;
    }

    public boolean isCommand(MessageReceivedEvent event) {
        User user = event.getAuthor();
        ChannelType channelType = event.getChannelType();
        Message message = event.getMessage();
        String command = message.getContentRaw().split(" ")[0];

        if (!(command.matches("^!\\w+") && ArrayUtils.contains(COMMAND_NAMES, command.substring(PREFIX_LENGTH)))) {
            return false;
        }

        if (user.isBot()) {
            return false;
        }

        if (ArrayUtils.contains(INVALID_CHANNEL_NAMES, channelType.name())) {
            return false;
        }

        return true;

    }

    public String getCommand(String contentRaw) {
        return contentRaw.split(" ")[0].substring(PREFIX_LENGTH, PREFIX_LENGTH + 1).toUpperCase() + contentRaw.split(" ")[0].substring(PREFIX_LENGTH + 1).toLowerCase();
    }

    public String[] parsingParam(String contentRaw, int langth) {

        return Arrays.copyOfRange(contentRaw.split(" "), 1, langth);
    }
    public String[] parsingParam(String contentRaw) {
        String[] params = contentRaw.split(" ");

        return Arrays.copyOfRange(params, 1, params.length);
    }

    public boolean isNumeric(String value) {
        if(Objects.isNull(value)) {
            return false;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
