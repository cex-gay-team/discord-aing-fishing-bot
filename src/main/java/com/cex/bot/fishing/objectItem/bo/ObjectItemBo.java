package com.cex.bot.fishing.objectItem.bo;

import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.Rods;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;

import java.util.List;

public interface ObjectItemBo {
    Rods getMyRods(FishingUser fishingUser);

    Baits getMyBatis(FishingUser fishingUser);

    void saveItem(InventoryItem inventoryItem);

    void useBatis(FishingUser myBatis);

    List<InventoryItem> getMyItems(int userId);

    int getMyItemsCount(int userId);

    String processBuyBaitsItem(String itemNo, FishingUser fishingUser);

    String processBuyRodsItem(String itemNo, FishingUser fishingUser);

    void changeInventoryItem(InventoryItem currentItem, InventoryItem changeItem);

    float sellItem(FishingUser fishingUser, InventoryItem inventoryNo);
}
