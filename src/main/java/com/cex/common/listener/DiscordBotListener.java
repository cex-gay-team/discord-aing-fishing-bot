package com.cex.common.listener;

import com.cex.bot.fishing.location.bo.LocationBo;
import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.log.bo.FishingLogBo;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.command.DiscordBaseCommand;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.UserStatus;
import com.cex.common.util.DiscordUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class DiscordBotListener extends ListenerAdapter {
    private static final String COMMAND_PREFIX = "fishingBot";
    private static final String COMMAND_POSTFIX = "Command";
    @Autowired
    private Map<String, DiscordBaseCommand> commandMap;

    @Autowired
    private DiscordBaseCommand fishingBotNotCommand;

    @Autowired
    private DiscordBaseCommand fishingBotHelpCommand;

    @Autowired
    private FishingUserBo fishingUserBo;

    @Autowired
    private LocationBo locationBo;

    @Autowired
    private FishingLogBo fishingLogBo;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        DiscordUtil discordUtil = DiscordUtil.getInstance();
        if (discordUtil.isCommand(event) && isFishingLocations(event.getTextChannel())) {
            DiscordBaseCommand command;
            FishingUser fishingUser = fishingUserBo.getFishingUserStatus(event.getAuthor());
            if(!fishingUserBo.isAttendance(fishingUser)) {
                fishingUserBo.attendance(fishingUser);
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "님," + LocalDate.now() + " 출석 진행하셨습니다~\n").queue();
            }
            if (fishingUser.getUserStatus() == UserStatus.WAIT) {
                String commandName = discordUtil.getCommand(event.getMessage().getContentRaw());
                command = commandMap.getOrDefault(COMMAND_PREFIX + commandName + COMMAND_POSTFIX, fishingBotNotCommand);
                command.execute(event);
            } else if (fishingUser.getUserStatus() == UserStatus.SIGN_UP) {
                event.getTextChannel().sendMessage("환영합니다. " + event.getAuthor().getAsMention() + "님.").queue();
                fishingBotHelpCommand.execute(event);

                fishingUser.setUserStatus(UserStatus.WAIT);
                fishingUserBo.updateFishingUserStatus(fishingUser);
            } else {
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "님. 현재 낚시중 상태이십니다. 낚시 완료 후 요청해주세요.").queue();
            }

            fishingLogBo.addCommandLog(fishingUser, event.getMessage().getContentRaw());
        }
    }

    private boolean isFishingLocations(TextChannel textChannel) {
        List<Location> fishingLocations = locationBo.getLocationList();
        JDA jda = textChannel.getJDA();
        boolean isFishingLocation = fishingLocations.stream()
                .anyMatch(location -> location.getDiscordId() == textChannel.getIdLong());
        if (!isFishingLocation) {
            textChannel.sendMessage("여기서는 낚시봇을 사용할수 없습니다. 아래 해당 채널에서 시도해주세요.").queue();
            for(Location location : fishingLocations) {
                textChannel.sendMessage(jda.getTextChannelById(location.getDiscordId()).getAsMention() + " 난이도 : " + location.getLevel()).queue();
            }

        }

        return isFishingLocation;
    }
}
