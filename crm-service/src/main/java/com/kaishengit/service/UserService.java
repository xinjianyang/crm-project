package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Dept;
import com.kaishengit.entity.User;
import com.kaishengit.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/11/6.
 */
public interface UserService {

    void save(User user);


    PageInfo<User> findAllUserWithDept(int pageNo);


    /**
     * @param deptName新部门名称
     * @throws ServiceException 添加部门,部门已存在抛出此异常
     */
    void saveNewDept(String deptName) throws ServiceException;

    List<Dept> findAllDept();

    PageInfo<User> findUserListByDeptId(int pageNo, int deptId);

    List<User> pageForUser(Map<String, Object> params);

    Long userCountByDeptId(Integer deptId);

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    User findUserByUserName(String userName);

    /**
     * 保存新增用户和部门之间的对应关系
     * @param uid
     * @param deptId
     */
    void saveUserDept(Integer uid, Integer deptId);
}
