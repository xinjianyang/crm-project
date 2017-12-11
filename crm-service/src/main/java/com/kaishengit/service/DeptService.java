package com.kaishengit.service;

import com.kaishengit.entity.Dept;

import java.util.List;

/**
 * @author NativeBoy
 */
public interface DeptService {

    /**
     * 查询所有部门
     * @return
     */
    List<Dept> findAllDept();
}
