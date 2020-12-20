package com.cex.bot.fishing.user.bo;

import com.cex.bot.fishing.user.mapper.FishingUserMapper;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.UserStatus;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FishingUserBoImpl implements FishingUserBo {
    public static final int ATTENDANCE_COIN = 100;
    @Autowired
    private FishingUserMapper fishingUserMapper;

    @Override
    public FishingUser getFishingUserStatus(User discordUser) {
        long discordUserId = Long.parseLong(discordUser.getId());
        FishingUser user = fishingUserMapper.selectUserStatus(discordUserId);

        if (Objects.isNull(user)) {
            user = new FishingUser();
            user.setDiscordId(discordUserId);
            user.setUserName(discordUser.getName());
            user.setUserStatus(UserStatus.SIGN_UP);

            fishingUserMapper.insertUser(user);
        }
        
        return user;
    }



    @Override
    public void updateFishingUserStatus(FishingUser fishingUser) {
        fishingUserMapper.updateUserStatus(fishingUser);
    }

    @Override
    public FishingUser getFishingUserByDiscordId(long discordId) {
        return fishingUserMapper.selectFishingUser(discordId);
    }

    @Override
    public void modifyUserItem(FishingUser fishingUser) {
        fishingUserMapper.updateUserItem(fishingUser);
    }

    @Override
    public boolean isAttendance(FishingUser fishingUser) {

        return fishingUserMapper.selectIsAttendanceToday(fishingUser);
    }

    @Override
    public void attendance(FishingUser fishingUser) {
        fishingUser.setCoin(fishingUser.getCoin() + ATTENDANCE_COIN);
        fishingUserMapper.updateUserCoin(fishingUser);
        fishingUserMapper.updateUserAttendanceDate(fishingUser);
    }
}
