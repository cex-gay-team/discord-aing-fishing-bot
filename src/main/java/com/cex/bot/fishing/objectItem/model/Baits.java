package com.cex.bot.fishing.objectItem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Baits {
    private int id;
    private int inventoryId;
    private String name;
    private float effect;
    private String remark;
    private float price;
    private int count;
}
