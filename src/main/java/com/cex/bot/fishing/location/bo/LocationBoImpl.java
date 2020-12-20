package com.cex.bot.fishing.location.bo;

import com.cex.bot.fishing.location.mapper.LocationMapper;
import com.cex.bot.fishing.location.model.Location;
import com.cex.bot.fishing.objectItem.model.Fishes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationBoImpl implements LocationBo{
    @Autowired
    private LocationMapper locationMapper;

    @Override
    public Location getLocation(long idLong) {
        return locationMapper.selectLocation(idLong);
    }

    @Override
    public List<Fishes> getLocationFishList(String rarity, long locationId) {
        return locationMapper.selectLocationFishListByRarity(rarity, locationId);
    }

    @Override
    @Cacheable("commonCache")
    public List<Location> getLocationList() {

        return locationMapper.selectLocationList();
    }

    @Override
    @Cacheable("commonCache")
    public List<Fishes> getLocationFishList(long locationId) {
        return locationMapper.selectLocationFishList(locationId);
    }
}
