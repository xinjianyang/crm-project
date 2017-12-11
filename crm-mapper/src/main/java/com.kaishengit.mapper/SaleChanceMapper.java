package com.kaishengit.mapper;

import com.kaishengit.entity.SaleChance;
import com.kaishengit.entity.SaleChanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SaleChanceMapper {
    long countByExample(SaleChanceExample example);

    int deleteByExample(SaleChanceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SaleChance record);

    int insertSelective(SaleChance record);

    List<SaleChance> selectByExample(SaleChanceExample example);

    SaleChance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SaleChance record, @Param("example") SaleChanceExample example);

    int updateByExample(@Param("record") SaleChance record, @Param("example") SaleChanceExample example);

    int updateByPrimaryKeySelective(SaleChance record);

    int updateByPrimaryKey(SaleChance record);

    List<SaleChance> findAllMyChance(Integer userId);

    SaleChance findSaleChanceById(Integer id);

}