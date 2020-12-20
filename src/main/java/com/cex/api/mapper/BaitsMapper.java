package com.cex.api.mapper;

import com.cex.api.model.BaitsModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface BaitsMapper {
    List<BaitsModel> selectBaits();
}
