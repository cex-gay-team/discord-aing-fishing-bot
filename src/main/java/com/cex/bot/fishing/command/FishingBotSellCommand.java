package com.cex.bot.fishing.command;

import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.common.util.DiscordSendUtil;
import com.cex.common.util.DiscordUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FishingBotSellCommand implements DiscordBaseCommand {
    private static final int MOUNTING_INVENTORY_MAX_NO = 2;
    @Autowired
    private DiscordSendUtil discordSendUtil;
    @Autowired
    private ObjectItemBo objectItemBo;
    @Autowired
    private FishingUserBo fishingUserBo;

    @Override
    public void execute(MessageReceivedEvent event) {
        DiscordUtil discordUtil = DiscordUtil.getInstance();
        String message = event.getMessage().getContentRaw();
        String[] parameters = discordUtil.parsingParam(message);
        FishingUser fishingUser = fishingUserBo.getFishingUserByDiscordId(event.getAuthor().getIdLong());
        List<InventoryItem> myInventoryList = objectItemBo.getMyItems(fishingUser.getUserId()).stream()
                .filter(inventoryItem -> inventoryItem.getInventoryNo() > MOUNTING_INVENTORY_MAX_NO).collect(Collectors.toList());

        float currentCoin = fishingUser.getCoin();
        for (String inventoryNo : parameters) {
            if (discordUtil.isNumeric(inventoryNo) && Integer.parseInt(inventoryNo) > MOUNTING_INVENTORY_MAX_NO) {
                Optional<InventoryItem> sellItem = myInventoryList.stream().filter(inventoryItem -> inventoryItem.getInventoryNo() == Integer.parseInt(inventoryNo))
                        .filter(inventoryItem -> !inventoryItem.isSellFlag()).findFirst();
                if (sellItem.isPresent()) {
                    InventoryItem item = sellItem.get();
                    objectItemBo.sellItem(fishingUser, item);
                    item.setSellFlag(true);
                }
            }
        }

        List<InventoryItem> selledInventoryList = myInventoryList.stream()
                .filter(InventoryItem::isSellFlag)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(selledInventoryList)) {
            float totalSellPrice = 0;
            message = fishingUser.getUserName() + "님!. 판매한 아이템 목록은 아래와 같아요.\n"
                    + "아이템 명 | 판매가격 \n";
            for (InventoryItem item : selledInventoryList) {
                totalSellPrice += item.getSellPrice();
                message += item.getObjectName() + " | " + item.getSellPrice() + "\n";
            }

            message += "이렇게 총  " + totalSellPrice + " 코인에 판매를 하고 현재 " + (currentCoin + totalSellPrice) + " 만큼 보유하시고 계세요.\n";
        } else {
            message = "판매는 !sell [인벤토리번호] [인벤토리번호]...로 판매할 수 있어요. \n 현재 판매가능 아이템은 아래와 같아요.\n"
                    + "No | 아이템명 | 가격 | 개수 \n"
                    + getPossibleSellListString(myInventoryList);
        }

        discordSendUtil.sendMessage(message, event.getTextChannel().getIdLong());
    }

    private String getPossibleSellListString(List<InventoryItem> myInventoryItemList) {
        StringBuilder message = new StringBuilder();
        if (myInventoryItemList.size() > 0) {
            for (InventoryItem inventoryItem : myInventoryItemList) {
                message.append(inventoryItem.getInventoryNo() + " | " + inventoryItem.getObjectName() + " | " + inventoryItem.getSellPrice() + " | " + inventoryItem.getCount() + "\n");
            }
        } else {
            message.append("판매가능한 아이템이 없어요.\n");
        }


        return message.toString();
    }
}
