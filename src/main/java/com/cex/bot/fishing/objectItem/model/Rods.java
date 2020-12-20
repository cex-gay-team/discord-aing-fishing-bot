package com.cex.bot.fishing.objectItem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rods {
    private int id;
    private String name;
    private float effect;
    private float weightLimit;
    private float price;
    private String remark;
}
