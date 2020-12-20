package com.cex.bot.fishing.objectItem.model;

import lombok.Data;

@Data
public class Fishes {
    private int id;
    private String name;
    private FishRarity rarity;
    private float defaultLength;
    private float defaultPrice;
    private boolean fish;
    private String remark;
    private String fileName;
}
