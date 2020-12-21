package com.cex.bot.fishing.log.bo;

import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;

public interface FishingLogBo {
    void addCommandLog(FishingUser fishingUser, String content);
    void addFishingResultLog(FishingUser fishingUser, InventoryItem inventoryItem, Location location);
}
