package com.cex.bot.fishing.command;

import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.objectItem.model.ObjectType;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.common.util.DiscordSendUtil;
import com.cex.common.util.DiscordUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FishingBotBaitCommand implements DiscordBaseCommand {
    private static final int BAITS_INVENTORY_NO = 2;
    @Autowired
    private FishingUserBo fishingUserBo;
    @Autowired
    private ObjectItemBo objectItemBo;
    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Override
    public void execute(MessageReceivedEvent event) {
        FishingUser fishingUser = fishingUserBo.getFishingUserByDiscordId(event.getAuthor().getIdLong());

        DiscordUtil discordUtil = DiscordUtil.getInstance();
        String[] parameters = discordUtil.parsingParam(event.getMessage().getContentRaw(), 2);
        String inventoryNumber = Objects.isNull(parameters[0]) ? "" : parameters[0];
        List<InventoryItem> myItemList = objectItemBo.getMyItems(fishingUser.getUserId());
        String message;

        if (discordUtil.isNumeric(inventoryNumber) && Integer.parseInt(inventoryNumber) > 2) {
            int inventoryNo = Integer.parseInt(inventoryNumber);
            Optional<InventoryItem> currentBaits = myItemList.stream().filter(inventoryItem -> inventoryItem.getInventoryNo() == BAITS_INVENTORY_NO).findFirst();
            Optional<InventoryItem> changeBaits = myItemList.stream().filter(inventoryItem -> inventoryItem.getInventoryNo() == inventoryNo)
                    .filter(inventoryItem -> StringUtils.equals(inventoryItem.getObjectType(), ObjectType.BAITS.getCode())).findFirst();

            if(changeBaits.isPresent()) {
                objectItemBo.changeInventoryItem(currentBaits.orElse(InventoryItem.builder().inventoryId(0).inventoryNo(BAITS_INVENTORY_NO).build()), changeBaits.get());

                fishingUser.setBaitsId(changeBaits.get().getObjectId());
                fishingUserBo.modifyUserItem(fishingUser);
                message = fishingUser.getUserName() + "님!" + changeBaits.get().getObjectName() + " 미끼를 장착했어요!\n";
            } else {
                message = "해당 번호의 인벤토리 아이템이 존재 하지 않거나, 미끼가 아니예요. \n" +
                        "현재 장가능한 미끼 목록은 아래와 같아요\n"
                        + getPossibleBatisListString(myItemList);
            }
        } else {
            message = "!bait [인벤토리번호] 로 요청해주세요. \n" +
                    " 현재 장착가능한 미끼 목록은 아래와 같아요\n" +
                    getPossibleBatisListString(myItemList);
        }
        discordSendUtil.sendMessage(message, event.getTextChannel().getIdLong());
    }

    private String getPossibleBatisListString(List<InventoryItem> myInventoryItemList) {
        StringBuilder message = new StringBuilder();
        List<InventoryItem> inventoryItems = myInventoryItemList.stream().filter(inventoryItem -> inventoryItem.getInventoryNo() != BAITS_INVENTORY_NO)
                .filter(inventoryItem -> StringUtils.equals(inventoryItem.getObjectType(), ObjectType.BAITS.getCode()))
                .collect(Collectors.toList());
        if(inventoryItems.size() > 0) {
            for (InventoryItem inventoryItem : inventoryItems) {
                message.append(inventoryItem.getInventoryNo() + " | " + inventoryItem.getObjectName() + " | " + inventoryItem.getCount() + "\n");
            }
        } else {
            message.append("장착 가능 미끼가 없네요. 미끼를 구매해주세요.");
        }



        return message.toString();
    }
}
