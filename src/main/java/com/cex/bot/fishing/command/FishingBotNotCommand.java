package com.cex.bot.fishing.command;

import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FishingBotNotCommand implements DiscordBaseCommand {
    @Autowired
    private DiscordSendUtil discordSendUtil;
    @Override
    public void execute(MessageReceivedEvent event) {
        discordSendUtil.sendMessage(event.getAuthor().getAsMention() + "님! 아직 구현안됬으니 기다리새우!", event.getTextChannel().getIdLong());
    }
}
