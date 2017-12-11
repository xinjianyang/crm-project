package com.kaishengit.service;

import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Disk;

import java.util.List;

/**
 * Created by User on 2017/11/16.
 */
public interface DiskService {

    /**
     * 获取公司网盘信息列表
     * @return 公司网盘信息列表
     */
    List<Disk> findAllDisk(Integer pid);

    Disk findById(Integer pid);

    void saveNewFolder(Disk disk);

    void deleteFolderById(Integer id, Admin admin);

}
