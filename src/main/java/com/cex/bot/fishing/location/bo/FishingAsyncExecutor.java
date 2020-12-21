package com.cex.bot.fishing.location.bo;

import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.log.bo.FishingLogBo;
import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.objectItem.model.*;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.bot.fishing.user.model.UserStatus;
import com.cex.common.util.DiscordSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class FishingAsyncExecutor {
    private static final int LOCATION_RANDOM_MAX_RANGE = 10;
    private static final int SECOND = 1000;
    private static final int MAX_WAIT_COUNT = 3;
    @Autowired
    private FishingUserBo fishingUserBo;

    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Autowired
    private ObjectItemBo objectItemBo;

    @Autowired
    private LocationBo locationBo;

    @Autowired
    private FishingLogBo fishingLogBo;

    @Async("fishingExecutor")
    @Transactional
    public void executeFishing(FishingUser fishingUser, Location location) {
        try {
            int locationDelay = location.getDelay();
            int locationLevel = location.getLevel();
            long locationId = location.getDiscordId();

            Random random = new Random();
            int wait_count = 1;
            boolean passWait = true;

            while (passWait) {
                discordSendUtil.sendMessage(fishingUser.getUserName() + "님; " + wait_count + "번째 시도!", locationId);
                Thread.sleep(locationDelay * SECOND);
                if (random.nextInt(LOCATION_RANDOM_MAX_RANGE) < locationLevel && wait_count < MAX_WAIT_COUNT) {
                    discordSendUtil.sendMessage(fishingUser.getUserName() + "님; 낚지 못했어유. 다시 시도할게요.", locationId);
                    wait_count++;
                } else {
                    passWait = false;
                }
            }
            ;

            if (wait_count == MAX_WAIT_COUNT) {
                discordSendUtil.sendMessage(fishingUser.getUserName() + "님; 최종 결과 알려드려유; " + MAX_WAIT_COUNT + "만큼 재시도했지만 낚지도 못했어유ㅜㅜ", locationId);
            } else {
                FishRarity rarity = FishRarity.getRandomFishRarity(random.nextInt(1000));
                List<Fishes> rareFishList = locationBo.getLocationFishList(rarity.name(), location.getLocationId());
                Fishes fishes = rareFishList.get(random.nextInt(rareFishList.size()));

                Baits baits = fishingUser.getBaits();
                Rods rods = fishingUser.getRods();

                float randomFishValue = (random.nextInt(100 + Math.round(rods.getWeightLimit() * 100)) + 50) * 0.01f;

                float succesCalu = 1f - Math.min((rods.getEffect() + baits.getEffect()) * 0.01f, 1f);
                if (random.nextFloat() > succesCalu) {
                    InventoryItem inventoryItem = InventoryItem.builder()
                            .fishUserId(fishingUser.getUserId())
                            .objectName(fishes.getName())
                            .objectId(fishes.getId())
                            .objectType(ObjectType.FISH.getCode())
                            .objectLength(fishes.getDefaultLength() * randomFishValue)
                            .objectPrice(fishes.getDefaultPrice() * randomFishValue)
                            .count(1)
                            .build();
                    objectItemBo.saveItem(inventoryItem);
                    fishingLogBo.addFishingResultLog(fishingUser, inventoryItem, location);
                    discordSendUtil.sendMessage(fishingUser.getUserName() + "님; 낚시 결과 알려드려유;" + fishes.getName() + "(" +  fishes.getRarity() + ") 을(를) 잡았어요!", locationId);
                    discordSendUtil.sendFile(fishes.getFileName(), locationId);
                } else {
                    discordSendUtil.sendMessage(fishingUser.getUserName() + "님; 낚시 결과 알려드려유;" + fishes.getName() + "(" +  fishes.getRarity() + ") 을(를) 낚을뻔 했는데,,, 실패!", locationId);
                }
            }
            objectItemBo.useBatis(fishingUser);
        } catch (Exception exception) {
            log.error("executeFishing aysnc task error param : {0}, {1} ", fishingUser, location, exception);
        } finally {
            fishingUser.setUserStatus(UserStatus.WAIT);
            fishingUserBo.updateFishingUserStatus(fishingUser);
        }
    }
}
