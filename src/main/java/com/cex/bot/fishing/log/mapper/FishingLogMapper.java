package com.cex.bot.fishing.log.mapper;

import com.cex.bot.fishing.log.model.CommandLog;
import com.cex.bot.fishing.log.model.FishingLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FishingLogMapper {
    void insertCommandLog(CommandLog commandLog);

    void insertFishingLog(FishingLog fishingLog);
}
