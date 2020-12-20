package com.cex.bot.fishing.command;

import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.ObjectType;
import com.cex.bot.fishing.objectItem.model.Rods;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FishingBotMeCommand implements DiscordBaseCommand {
    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Autowired
    private FishingUserBo fishingUserBo;

    @Autowired
    private ObjectItemBo objectItemBo;

    @Override
    public void execute(MessageReceivedEvent event) {
        FishingUser fishingUser = fishingUserBo.getFishingUserByDiscordId(event.getAuthor().getIdLong());
        Baits myBaits = objectItemBo.getMyBatis(fishingUser);
        Rods myRods = objectItemBo.getMyRods(fishingUser);
        List<InventoryItem> myInventoryList = objectItemBo.getMyItems(fishingUser.getUserId()).stream().filter(inventoryItem -> inventoryItem.getInventoryNo() > 2).collect(Collectors.toList());

        StringBuilder message = new StringBuilder(fishingUser.getUserName() + "님. 장비 및 가지고 있는 아이템 목록 보여드릴게요.\n");

        message.append("현재 보유 코인 : " + fishingUser.getCoin() + "\n")
                .append("장착한 낚시대 : " + (Objects.isNull(myRods) ? "" : myRods.getName()) + "\n")
                .append("장착한 미끼 : " + (Objects.isNull(myBaits) ? "" : myBaits.getName() + "(" + myBaits.getCount() + ")") + "\n")
                .append("장착한 아이템을 제외한 아이템 목록 \n")
        .append("No | 아이템명 | 아이템타입 | 길이 | 보유개수 | 총 (" + myInventoryList.size() + ")\n");


        for(InventoryItem item : myInventoryList) {
            message.append(item.getInventoryNo() + " | " + item.getObjectName() + " | " + ObjectType.getObjectTypeByCode(item.getObjectType()).getKrName() + " | " + item.getObjectLength() + " | " + item.getCount() + "\n");
        }
        discordSendUtil.sendMessage(message.toString(), event.getTextChannel().getIdLong());
    }
}
