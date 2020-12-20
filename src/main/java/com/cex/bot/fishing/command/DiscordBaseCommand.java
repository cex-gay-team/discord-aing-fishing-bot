package com.cex.bot.fishing.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface DiscordBaseCommand {
    void execute(MessageReceivedEvent event);
}
