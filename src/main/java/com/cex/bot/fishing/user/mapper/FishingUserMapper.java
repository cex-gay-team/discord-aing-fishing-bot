package com.cex.bot.fishing.user.mapper;

import com.cex.bot.fishing.user.model.FishingUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FishingUserMapper {
    FishingUser selectUserStatus(long userId);

    void insertUser(FishingUser fishingUser);

    void updateUserStatus(FishingUser fishingUser);

    FishingUser selectFishingUser(long discordId);

    void updateUserCoin(FishingUser fishingUser);
    void updateUserItem(FishingUser fishingUser);

    boolean selectIsAttendanceToday(FishingUser fishingUser);

    void updateUserAttendanceDate(FishingUser fishingUser);
}
