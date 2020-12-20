package com.cex.bot.fishing.objectItem.bo;

import com.cex.bot.fishing.objectItem.mapper.ObjectItemMapper;
import com.cex.bot.fishing.objectItem.model.Baits;
import com.cex.bot.fishing.objectItem.model.ObjectType;
import com.cex.bot.fishing.objectItem.model.Rods;
import com.cex.bot.fishing.user.mapper.FishingUserMapper;
import com.cex.bot.fishing.user.model.FishingUser;
import com.cex.bot.fishing.user.model.InventoryItem;
import com.cex.common.util.DiscordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ObjectItemBoImpl implements ObjectItemBo {
    private static int NO_BAITS_ID = 0;
    private static int NO_MOUNT_TYPE = 0;

    @Autowired
    private ObjectItemMapper objectItemMapper;

    @Autowired
    private FishingUserMapper fishingUserMapper;

    @Override
    public void saveItem(InventoryItem inventoryItem) {
        List<InventoryItem> myInventoryItems = objectItemMapper.selectMyInventoryItemList(inventoryItem.getFishUserId());

        int searchCount = 3;

        for (InventoryItem item : myInventoryItems) {

            if (item.getInventoryNo() != searchCount && item.getInventoryNo() > 2) {
                break;
            } else if (item.getInventoryNo() > 2) {
                searchCount++;
            }
        }

        inventoryItem.setInventoryNo(searchCount);

        objectItemMapper.insertInventory(inventoryItem);
    }

    @Override
    public void useBatis(FishingUser fishingUser) {
        if (fishingUser.getBaits().getCount() > 1) {
            objectItemMapper.updateDecreaseBaitsItem(fishingUser.getBaits());
        } else {
            objectItemMapper.deleteBaitsItem(fishingUser.getBaits());
            fishingUser.setBaitsId(NO_BAITS_ID);
            fishingUserMapper.updateUserItem(fishingUser);
        }

    }

    @Override
    public Rods getMyRods(FishingUser fishingUser) {
        return objectItemMapper.selectMyRodsInfo(fishingUser);
    }

    @Override
    public Baits getMyBatis(FishingUser fishingUser) {
        return objectItemMapper.selectMyBatisInfo(fishingUser);
    }

    @Override
    public List<InventoryItem> getMyItems(int userId) {
        return objectItemMapper.selectMyInventoryItemList(userId);
    }

    @Override
    @Transactional
    public String processBuyBaitsItem(String itemNoTypeString, FishingUser fishingUser) {
        if (StringUtils.isEmpty(itemNoTypeString)) {
            List<Baits> baitsList = objectItemMapper.selectBatisInfoList();
            StringBuilder message = new StringBuilder("상점내 미끼 목록 보여드려요.\n");
            message.append("No | 미끼명 | 가격\n");

            for (Baits baits : baitsList) {
                message.append(baits.getId() + " | " + baits.getName() + " | " + baits.getPrice() + "\n");
            }

            return message.toString();
        }

        if (DiscordUtil.getInstance().isNumeric(itemNoTypeString)) {
            int itemNo = Integer.parseInt(itemNoTypeString);

            Baits buyingBatis = objectItemMapper.selectBatisInfo(itemNo);
            if (Objects.isNull(buyingBatis)) {
                return fishingUser.getUserName() + "님, 해당 아이템 번호를 가진 미끼가 없네요^^ 다시 확인하고 요청하세요.";

            } else if (fishingUser.getCoin() >= buyingBatis.getPrice()) {
                InventoryItem item = InventoryItem.builder()
                        .fishUserId(fishingUser.getUserId())
                        .objectId(buyingBatis.getId())
                        .objectType(ObjectType.BAITS.getCode())
                        .objectPrice(buyingBatis.getPrice())
                        .objectLength(0)
                        .count(30)
                        .build();
                saveItem(item);

                fishingUser.setCoin(fishingUser.getCoin() - buyingBatis.getPrice());
                fishingUserMapper.updateUserCoin(fishingUser);

                return fishingUser.getUserName() + "님, " + buyingBatis.getName() + "을 구입하셨습니다. 감사합니다! 남은 코인은 " + fishingUser.getCoin() + "입니다.";
            } else {
                return fishingUser.getUserName() + "님, 돈이 부족해요 현재 가진 코인은 " + fishingUser.getCoin() + "이고 "
                        + buyingBatis.getName() + "은 " + buyingBatis.getPrice() + "입니다. 돈을 좀더 모아보세요!";
            }

        } else {
            return fishingUser.getUserName() + "님, 2번째에는 아이템 번호를 입력하셔야 해요!";
        }
    }

    @Override
    public String processBuyRodsItem(String itemNoTypeString, FishingUser fishingUser) {
        if (StringUtils.isEmpty(itemNoTypeString)) {
            List<Rods> rodsList = objectItemMapper.selectRodsInfoList();
            StringBuilder message = new StringBuilder("상점내 낚시대 목록 보여드려요.\n");
            message.append("No | 낚시대명 | 가격\n");

            for (Rods rod : rodsList) {
                message.append(rod.getId() + " | " + rod.getName() + " | " + rod.getPrice() + "\n");
            }

            return message.toString();
        }

        if (DiscordUtil.getInstance().isNumeric(itemNoTypeString)) {
            int itemNo = Integer.parseInt(itemNoTypeString);

            Rods buyingRods = objectItemMapper.selectRodsInfo(itemNo);
            if (Objects.isNull(buyingRods)) {
                return fishingUser.getUserName() + "님, 해당 아이템 번호를 가진 낚시대가 없네요^^ 다시 확인하고 요청하세요.";

            } else if (fishingUser.getCoin() >= buyingRods.getPrice()) {
                InventoryItem item = InventoryItem.builder()
                        .fishUserId(fishingUser.getUserId())
                        .objectId(buyingRods.getId())
                        .objectType(ObjectType.ROD.getCode())
                        .objectPrice(buyingRods.getPrice())
                        .objectLength(0)
                        .count(1)
                        .build();
                saveItem(item);

                fishingUser.setCoin(fishingUser.getCoin() - buyingRods.getPrice());
                fishingUserMapper.updateUserCoin(fishingUser);

                return fishingUser.getUserName() + "님, " + buyingRods.getName() + "을 구입하셨습니다. 감사합니다! 남은 코인은 " + fishingUser.getCoin() + "입니다.";
            } else {
                return fishingUser.getUserName() + "님, 돈이 부족해요 현재 가진 코인은 " + fishingUser.getCoin() + "이고 "
                        + buyingRods.getName() + "은 " + buyingRods.getPrice() + "입니다. 돈을 좀더 모아보세요!";
            }
        } else {
            //번호가 아니라 다른거 넣었으므로 경고 메세지
            return fishingUser.getUserName() + "님, 2번째에는 아이템 번호를 입력하셔야 해요!";
        }
    }

    @Override
    @Transactional
    public void changeInventoryItem(InventoryItem currentItem, InventoryItem changeItem) {
        if (currentItem.getInventoryId() == NO_MOUNT_TYPE) {
            changeItem.setInventoryNo(currentItem.getInventoryNo());

            objectItemMapper.updateInventoryItemNo(changeItem);
        } else {
            int changeInventoryNo1 = currentItem.getInventoryNo();
            int changeInventoryNo2 = changeItem.getInventoryNo();
            currentItem.setInventoryNo(changeInventoryNo2);
            changeItem.setInventoryNo(changeInventoryNo1);

            objectItemMapper.updateInventoryItemNo(currentItem);
            objectItemMapper.updateInventoryItemNo(changeItem);
        }
    }

    @Override
    @Transactional
    public float sellItem(FishingUser fishingUser, InventoryItem inventoryItem) {
        objectItemMapper.deleteInventoryItem(inventoryItem);
        fishingUser.setCoin(fishingUser.getCoin() + inventoryItem.getSellPrice());
        fishingUserMapper.updateUserCoin(fishingUser);

        return inventoryItem.getSellPrice();
    }

    @Override
    public int getMyItemsCount(int userId) {
        return objectItemMapper.selectMyInventoryItemCount(userId);
    }
}
