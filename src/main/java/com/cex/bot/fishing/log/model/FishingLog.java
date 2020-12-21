package com.cex.bot.fishing.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FishingLog {
    private int fishingUserId;
    private int fishId;
    private float length;
    private float price;
    private int locationId;
    private int rodId;
    private int baitId;
}
