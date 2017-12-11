package com.kaishengit.mapper;

import com.kaishengit.entity.User;
import com.kaishengit.entity.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    List<User> findAllUserWithDept();

    List<User> findUserListWithDeptId(@Param("deptId") Integer deptId);

    List<User> findByDeptId(@Param("userName") String userName,@Param("deptId") Integer deptId, @Param("start") Integer start,@Param("length") Integer length);

    Long countByDeptId(@Param("deptId") Integer deptId);

}