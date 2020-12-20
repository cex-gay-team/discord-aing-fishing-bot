package com.cex.bot.fishing.user.bo;

import com.cex.bot.fishing.user.model.FishingUser;
import net.dv8tion.jda.api.entities.User;

public interface FishingUserBo {
    FishingUser getFishingUserStatus(User discordUser);
    void updateFishingUserStatus(FishingUser fishingUser);

    FishingUser getFishingUserByDiscordId(long discordId);

    void modifyUserItem(FishingUser fishingUser);

    boolean isAttendance(FishingUser fishingUser);

    void attendance(FishingUser fishingUser);
}
