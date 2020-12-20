package com.cex.bot.fishing.location.bo;

import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.objectItem.model.Fishes;

import java.util.List;

public interface LocationBo {
    Location getLocation(long idLong);

    List<Fishes> getLocationFishList(String rarity, long locationId);

    List<Location> getLocationList();

    List<Fishes> getLocationFishList(long locationId);
}
