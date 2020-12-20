package com.cex.bot.fishing.command;

import com.cex.bot.fishing.location.bo.FishingAsyncExecutor;
import com.cex.bot.fishing.location.bo.LocationBo;
import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.Rods;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.bot.fishing.user.model.UserStatus;
import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FishingBotFishingCommand implements DiscordBaseCommand {
    private static final int MAX_ITEM_SIZE = 20;
    @Autowired
    private FishingAsyncExecutor fishingAsyncExecutor;

    @Autowired
    private FishingUserBo fishingUserBo;

    @Autowired
    private LocationBo locationBo;

    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Autowired
    private ObjectItemBo objectItemBo;

    @Override
    public void execute(MessageReceivedEvent event) {
        FishingUser fishingUser = fishingUserBo.getFishingUserByDiscordId(event.getAuthor().getIdLong());
        Location location = locationBo.getLocation(event.getTextChannel().getIdLong());
        if(validate(fishingUser)) {
            fishingUser.setUserStatus(UserStatus.FISHING);
            fishingUserBo.updateFishingUserStatus(fishingUser);

            fishingAsyncExecutor.executeFishing(fishingUser, location);

            discordSendUtil.sendMessage(event.getAuthor().getAsMention() + "님. 낚시를 시작할게유~", location.getDiscordId());
        } else {
            discordSendUtil.sendMessage(event.getAuthor().getAsMention() + "님. 낚시대, 미끼를 장착했는지, 인벤토리내 아이템 개수가 20개 꽉차있는지 확인하고 다시 시도해 주세요.", location.getDiscordId());
        }
    }

    private boolean validate(FishingUser fishingUser) {
        Rods rods  = objectItemBo.getMyRods(fishingUser);
        if(Objects.isNull(rods)) {
            return false;
        }
        Baits baits = objectItemBo.getMyBatis(fishingUser);

        if(Objects.isNull(baits)) {
            return false;
        }
        List<InventoryItem> items = objectItemBo.getMyItems(fishingUser.getUserId());
        if(items.size() >= MAX_ITEM_SIZE) {
            return false;
        }
        fishingUser.setRods(rods);
        fishingUser.setBaits(baits);
        fishingUser.setInventoryItemList(items);

        return true;
    }
}
