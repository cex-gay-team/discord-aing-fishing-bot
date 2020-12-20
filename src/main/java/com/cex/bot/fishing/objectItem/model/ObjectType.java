package com.cex.bot.fishing.objectItem.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ObjectType {
    ROD("R", "낚시대"), FISH("F", "아이템"), BAITS("B", "미끼");
    @Getter
    String code;
    @Getter
    String krName;
    private static Map<String, ObjectType> OBJECT_TYPE_MAP_KEY_CODE = new HashMap<>();
    static {
        for(ObjectType objectType : ObjectType.values()) {
            OBJECT_TYPE_MAP_KEY_CODE.put(objectType.getCode(), objectType);
        }
    }
    ObjectType(String code, String krName) {
        this.code = code;
        this.krName = krName;
    }

    public static ObjectType getObjectTypeByCode(String code) {
        return OBJECT_TYPE_MAP_KEY_CODE.get(code);
    }
}
