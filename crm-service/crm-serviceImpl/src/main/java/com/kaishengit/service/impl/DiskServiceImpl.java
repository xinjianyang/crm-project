package com.kaishengit.service.impl;

import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Disk;
import com.kaishengit.entity.DiskExample;
import com.kaishengit.mapper.DiskMapper;
import com.kaishengit.service.DiskService;
import com.kaishengit.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/11/16.
 */
@Service
public class DiskServiceImpl implements DiskService {

    @Autowired
    private DiskMapper diskMapper;


    /**
     * 获取公司网盘信息列表
     * @return 公司网盘信息列表
     */
    @Override
    public List<Disk> findAllDisk(Integer pid) {
        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andPIdEqualTo(pid);
        diskExample.setOrderByClause("id asc");
        return diskMapper.selectByExample(diskExample);
    }

    @Override
    public Disk findById(Integer id) {
        return diskMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveNewFolder(Disk disk) {

        disk.setType(Disk.DISK_TYPE_FOLDER);
        disk.setUpdateTime(new Date());
        diskMapper.insertSelective(disk);
    }

    @Override
    public void deleteFolderById(Integer id, Admin admin) {
        Disk disk = diskMapper.selectByPrimaryKey(id);
        if(disk == null){
            throw new ServiceException("文件不存在或已被删除");
        }
        if (disk.getUserId().equals(admin.getId())){
            diskMapper.deleteByPrimaryKey(id);
        }
        throw new ServiceException("您不是该文件上传者,无权删除");
    }
}
