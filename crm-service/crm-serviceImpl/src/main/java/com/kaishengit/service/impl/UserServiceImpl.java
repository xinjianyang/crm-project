package com.kaishengit.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.*;

import com.kaishengit.mapper.DeptMapper;
import com.kaishengit.mapper.UserDeptMapper;
import com.kaishengit.mapper.UserMapper;
import com.kaishengit.service.UserService;
import com.kaishengit.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 *
 * @author xinjian
 * @date 2017/11/6
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserDeptMapper userDeptMapper;


    private static final Integer COMPANY_ID = 1;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void save(User user) {

        userMapper.insertSelective(user);
    }

    @Override
    public PageInfo<User> findAllUserWithDept(int pageNo) {

        PageHelper.startPage(pageNo, 100);
        List<User> userList = userMapper.findAllUserWithDept();

        return new PageInfo<>(userList);
    }

    @Override
    public void saveNewDept(String deptName)throws ServiceException {

        //判断deptName是否存在
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andDeptNameEqualTo(deptName);
        List<Dept> deptList =  deptMapper.selectByExample(deptExample);

        Dept dept = null;
        if(deptList != null && !deptList.isEmpty()){
            dept = deptList.get(0);
        }

        if(dept != null){
            throw new ServiceException("该部门已存在");
        }

        dept = new Dept();
        dept.setDeptName(deptName);
        dept.setpId(COMPANY_ID);

        deptMapper.insertSelective(dept);

        logger.info("添加新部门{}" + deptName);
    }

    @Override
    public List<Dept> findAllDept() {
        return deptMapper.selectByExample(new DeptExample());

    }

    @Override
    public PageInfo<User> findUserListByDeptId(int pageNo ,int deptId) {
        PageHelper.startPage(pageNo, 100);
        List<User> userList = userMapper.findUserListWithDeptId(deptId);

        return new PageInfo<>(userList);
    }

    @Override
    public List<User> pageForUser(Map<String, Object> params) {

        Integer start = (Integer) params.get("start");
        Integer length = (Integer) params.get("length");
        Integer deptId = (Integer) params.get("deptId");
        String userName = (String) params.get("userName");

        if(deptId == null || COMPANY_ID.equals(deptId)){
            deptId = null;
        }

        List<User> userList = userMapper.findByDeptId(userName,deptId,start,length);


        return userList;
    }

    @Override
    public Long userCountByDeptId(Integer deptId) {

        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }
        return userMapper.countByDeptId(deptId);

    }

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(userName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList != null && !userList.isEmpty()){
            return userList.get(0);
        }
        return null;
    }

    /**
     * 保存新增用户和部门之间的对应关系
     *
     * @param uid
     * @param deptId
     */
    @Override
    public void saveUserDept(Integer uid, Integer deptId) {
        UserDeptKey userDeptKey = new UserDeptKey();
        userDeptKey.setDid(deptId);
        userDeptKey.setUid(uid);
        userDeptMapper.insertSelective(userDeptKey);
    }
}
