package com.cex.bot.fishing.command;

import com.cex.bot.fishing.objectItem.bo.ObjectItemBo;
import com.cex.bot.fishing.user.bo.FishingUserBo;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.common.util.DiscordSendUtil;
import com.cex.common.util.DiscordUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class FishingBotBuyCommand implements DiscordBaseCommand {
    private static final String[] BAIT_ARRAYS = {"미끼", "B", "BAITS", "BAIT"};
    private static final String[] RODS_ARRAYS = {"낚시대", "R", "RODS", "ROD"};
    private static final int MAX_INVENTORY_SIZE = 20;
    @Autowired
    private DiscordSendUtil discordSendUtil;
    @Autowired
    private ObjectItemBo objectItemBo;
    @Autowired
    private FishingUserBo fishingUserBo;

    @Override
    @Transactional
    public void execute(MessageReceivedEvent event) {
        DiscordUtil discordUtil = DiscordUtil.getInstance();
        String message = event.getMessage().getContentRaw();
        String[] parameters = discordUtil.parsingParam(message, 3);

        String itemType = Objects.isNull(parameters[0]) ? "" : parameters[0];
        String itemNo = Objects.isNull(parameters[1]) ? "" : parameters[1];
        FishingUser fishingUser = fishingUserBo.getFishingUserByDiscordId(event.getAuthor().getIdLong());
        int currentInventorySize = objectItemBo.getMyItemsCount(fishingUser.getUserId());
        if (currentInventorySize == MAX_INVENTORY_SIZE) {
            message = "물건을 살수 없습니다. 현재 보유한 아이템 개수가 MAX 수치인 " + MAX_INVENTORY_SIZE + "에 도달했습니다. !sell 명령어를 통해 아이템을 팔아주세요.\n";
        } else if (ArrayUtils.contains(BAIT_ARRAYS, itemType.toUpperCase())) {
            message = objectItemBo.processBuyBaitsItem(itemNo, fishingUser);
        } else if (ArrayUtils.contains(RODS_ARRAYS, itemType.toUpperCase())) {
            message = objectItemBo.processBuyRodsItem(itemNo, fishingUser);
        } else {
            message = "잘못된 상점 요청입니다. !buy [아이템타입] [아이템 목록에서 제일 처음 번호]로 아이템을 구입할수 있고, !buy [타입] 의 경우 구입할수 있는 아이템 목록을 볼수 있습니다.\n"
                    + "아이템타입으로는 낚시대와 미끼 두종류로 나뉘며, 낚시대는 [낚시대/r/rods]  미끼는 [미끼/b/baits] 로 입력하시면 됩니다.\n";
        }
        discordSendUtil.sendMessage(message, event.getTextChannel().getIdLong());
    }
}
