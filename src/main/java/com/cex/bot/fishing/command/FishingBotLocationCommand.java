package com.cex.bot.fishing.command;

import com.cex.bot.fishing.location.bo.LocationBo;
import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.objectItem.model.Fishes;
import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FishingBotLocationCommand implements DiscordBaseCommand {
    @Autowired
    private LocationBo locationBo;

    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Override
    public void execute(MessageReceivedEvent event) {
        Location location = locationBo.getLocation(event.getTextChannel().getIdLong());
        List<Fishes> locationFishList = locationBo.getLocationFishList(location.getLocationId());
        StringBuilder message = new StringBuilder(event.getAuthor().getAsMention() + "님!"+ location.getLocationName() + "낚시터 정보를 공유드려요. \n");

        for(Fishes fish : locationFishList) {
            message.append(fish.getName() + ", ")
                    .append(fish.getRarity() + ", ")
                    .append("평균길이: " + fish.getDefaultLength() + ", ")
                    .append("평균가격: " + fish.getDefaultPrice() + "\n");
        }

        discordSendUtil.sendMessage(message.toString(), event.getTextChannel().getIdLong());
    }
}
