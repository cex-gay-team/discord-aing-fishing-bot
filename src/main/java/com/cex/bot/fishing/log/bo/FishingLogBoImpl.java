package com.cex.bot.fishing.log.bo;

import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.log.model.CommandLog;
import com.cex.bot.fishing.log.mapper.FishingLogMapper;
import com.cex.bot.fishing.log.model.FishingLog;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.common.util.DiscordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class FishingLogBoImpl implements FishingLogBo {
    @Autowired
    private FishingLogMapper fishingLogMapper;

    @Override
    public void addCommandLog(FishingUser fishingUser, String content) {
        CommandLog commandLog = CommandLog.builder()
                .userId(fishingUser.getUserId())
                .command(DiscordUtil.getInstance().getCommand(content).toLowerCase())
                .parameter(Arrays.stream(DiscordUtil.getInstance().parsingParam(content)).reduce(String::concat).orElse(""))
                .build();
        fishingLogMapper.insertCommandLog(commandLog);
    }

    @Override
    public void addFishingResultLog(FishingUser fishingUser, InventoryItem inventoryItem, Location location) {
        FishingLog fishingLog = FishingLog.builder()
                .fishingUserId(fishingUser.getUserId())
                .fishId(inventoryItem.getObjectId())
                .baitId(fishingUser.getBaitsId())
                .rodId(fishingUser.getRodId())
                .locationId(location.getLocationId())
                .length(inventoryItem.getObjectLength())
                .price(inventoryItem.getObjectPrice())
                .build();

        fishingLogMapper.insertFishingLog(fishingLog);

    }
}
