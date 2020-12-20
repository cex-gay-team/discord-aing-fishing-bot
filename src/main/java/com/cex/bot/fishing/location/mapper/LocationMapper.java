package com.cex.bot.fishing.location.mapper;

import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.objectItem.model.Fishes;
import com.cex.bot.fishing.user.model.FishingUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LocationMapper {
    Location selectLocation(long idLong);

    List<Fishes> selectLocationFishListByRarity(@Param("rarity") String rarity, @Param("locationId") long locationId);

    List<Location> selectLocationList();

    List<Fishes> selectLocationFishList(long locationId);
}
