package com.kaishengit.service.impl;

import com.kaishengit.entity.Dept;
import com.kaishengit.entity.DeptExample;
import com.kaishengit.mapper.DeptMapper;
import com.kaishengit.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author NativeBoy
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;


    /**
     * 查询所有部门
     *
     * @return
     */
    @Override
    public List<Dept> findAllDept() {

        return deptMapper.selectByExample(new DeptExample());
    }
}
