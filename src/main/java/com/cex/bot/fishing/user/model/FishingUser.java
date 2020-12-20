package com.cex.bot.fishing.user.model;

import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.Rods;
import lombok.Data;

import java.util.List;

@Data
public class FishingUser {
    private int userId;
    private String userName;
    private float coin;
    private int baitsId;
    private int rodId;
    private long discordId;
    private UserStatus userStatus;
    private Baits baits;
    private Rods rods;
    private List<InventoryItem> inventoryItemList;
}
