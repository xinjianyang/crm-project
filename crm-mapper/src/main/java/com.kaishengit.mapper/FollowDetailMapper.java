package com.kaishengit.mapper;

import com.kaishengit.entity.FollowDetail;
import com.kaishengit.entity.FollowDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FollowDetailMapper {
    long countByExample(FollowDetailExample example);

    int deleteByExample(FollowDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FollowDetail record);

    int insertSelective(FollowDetail record);

    List<FollowDetail> selectByExample(FollowDetailExample example);

    FollowDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FollowDetail record, @Param("example") FollowDetailExample example);

    int updateByExample(@Param("record") FollowDetail record, @Param("example") FollowDetailExample example);

    int updateByPrimaryKeySelective(FollowDetail record);

    int updateByPrimaryKey(FollowDetail record);
}