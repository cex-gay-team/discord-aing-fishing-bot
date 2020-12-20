package com.cex.bot.fishing.objectItem.mapper;

import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.Rods;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ObjectItemMapper {
    Baits selectMyBatisInfo(FishingUser batisId);

    Rods selectMyRodsInfo(FishingUser rodsId);

    void insertInventory(InventoryItem inventoryItem);

    List<InventoryItem> selectMyInventoryItemList(int fishUserId);

    void updateDecreaseBaitsItem(Baits myBatis);

    void deleteBaitsItem(Baits myBatis);

    Baits selectBatisInfo(int itemNo);

    List<Baits> selectBatisInfoList();

    List<Rods> selectRodsInfoList();

    Rods selectRodsInfo(int itemNo);

    void updateInventoryItemNo(InventoryItem changeItem);

    void deleteInventoryItem(InventoryItem inventoryItem);

    int selectMyInventoryItemCount(int userId);
}
