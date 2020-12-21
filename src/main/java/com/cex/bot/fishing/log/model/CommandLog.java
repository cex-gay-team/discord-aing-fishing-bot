package com.cex.bot.fishing.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandLog {
    private int userId;
    private String command;
    private String parameter;
}
