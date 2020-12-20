package com.cex.bot.fishing.objectItem.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FishRarity {
    SSS(2)
    , SS(8)
    , S(15)
    , A(45)
    , B(130)
    , C(300)
    , D(500);
    private static final List<FishRarity> sortedFishRarity;

    static {
        sortedFishRarity = Arrays.stream(FishRarity.values())
                                .sorted(Comparator.comparingInt(FishRarity::getProbability))
                                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Getter
    int probability;
    FishRarity(int probability) {
        this.probability = probability;
    }

    public static FishRarity getRandomFishRarity(int randomProbability) {
        FishRarity rarity = FishRarity.D;
        int probabilityRange = 0;
        for(FishRarity fishRarity : sortedFishRarity) {
            probabilityRange += fishRarity.getProbability();;
            if(randomProbability < probabilityRange) {
                rarity = fishRarity;
                break;
            }
        }

        return rarity;
    }

}
