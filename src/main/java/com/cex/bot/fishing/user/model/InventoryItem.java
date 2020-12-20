package com.cex.bot.fishing.user.model;

import com.cex.bot.fishing.objectItem.model.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {
    private int inventoryId;
    private int fishUserId;
    private int objectId;
    private String objectName;
    private String objectType;
    private float objectPrice;
    private float objectLength;
    private int count;
    private int inventoryNo;
    private boolean sellFlag;
    public float getSellPrice() {
        if(ObjectType.getObjectTypeByCode(this.objectType) == ObjectType.BAITS) {
            return Math.round(this.objectPrice / 30.0f * (float)count);
        } else {
            return this.objectPrice;
        }
    }
}
