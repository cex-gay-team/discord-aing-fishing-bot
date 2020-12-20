package com.cex.bot.fishing.command;

import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FishingBotPingCommand implements DiscordBaseCommand {
    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Override
    public void execute(MessageReceivedEvent event) {
        discordSendUtil.sendMessage("Pong!", event.getTextChannel().getIdLong());
    }
}
