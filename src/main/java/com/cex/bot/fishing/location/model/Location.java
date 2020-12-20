package com.cex.bot.fishing.location.model;

import lombok.Data;

@Data
public class Location {
    private int locationId;
    private long discordId;
    private String locationName;
    private String remark;
    private int delay;
    private int level;
}
