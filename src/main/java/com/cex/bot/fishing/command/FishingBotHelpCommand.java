package com.cex.bot.fishing.command;

import com.cex.common.util.DiscordSendUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FishingBotHelpCommand implements DiscordBaseCommand {
    @Autowired
    private DiscordSendUtil discordSendUtil;

    @Override
    public void execute(MessageReceivedEvent event) {
        String helpText = "낚시봇 사용법 설명드려요.\n" +
                "!fishing 낚시를 시작합니다.\n" +
                "!bait [인벤토리번호] 미끼를 장착합니다.\n" +
                "!rod [인벤토리번호] 낚시대를 장착합니다.\n" +
                "!location 낚시터 정보를 보여줍니다!\n" +
                "!buy [아이템타입] [아이템번호] 아이템을 구매합니다. 입력하지 않을 시 아이템 구매가능 목록을 보여줍니다\n" +
                "!sell [인벤토리번호] 아이템을 판매합니다. 번호를 입력안하면 판매 가능 목록을 보여줍니다\n" +
                "!me 장착한 장비. 보유한 아이템 목록을 보여줍니다.\n" +
                "!help 도움말을 보여줍니다.\n" +
                "자세한것은 해당 명령어를 사용해 보세요.\n";

        discordSendUtil.sendMessage(helpText, event.getTextChannel().getIdLong());
    }
}
