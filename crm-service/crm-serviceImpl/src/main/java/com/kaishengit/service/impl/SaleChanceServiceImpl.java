package com.kaishengit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.*;
import com.kaishengit.mapper.CustomerMapper;
import com.kaishengit.mapper.FollowDetailMapper;
import com.kaishengit.mapper.SaleChanceMapper;
import com.kaishengit.service.SaleChanceService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/11/13.
 */
@Service
public class SaleChanceServiceImpl implements SaleChanceService {

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private FollowDetailMapper followDetailMapper;

    @Value("#{'${saleChance.progress}'.split(',')}")
    private List<String> progressList;


    @Override
    public List<String> findAllProgress(){
        return progressList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewSaleChance(SaleChance saleChance, Admin admin) {
        Customer customer = customerMapper.selectByPrimaryKey(saleChance.getCustomerId());
        customer.setUpdatetime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        saleChance.setCreatetime(new Date());
        saleChance.setUserId(admin.getId());
        //需要updateTime字段可以从customer获取
        saleChanceMapper.insertSelective(saleChance);

        if(StringUtils.isNotEmpty(saleChance.getContent())){
            FollowDetail followDetail = new FollowDetail();
            followDetail.setContent(saleChance.getContent());
            followDetail.setSaleId(saleChance.getId());
            followDetail.setCreatetime(new Date());
            followDetailMapper.insertSelective(followDetail);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewFollowDetail(FollowDetail followDetail, Integer customerId) {
        followDetail.setCreatetime(new Date());
        followDetailMapper.insertSelective(followDetail);
        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        customer.setUpdatetime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    @Override
    public List<FollowDetail> findAllFollowBySaleId(Integer id) {

        FollowDetailExample followDetailExample = new FollowDetailExample();
        followDetailExample.createCriteria().andSaleIdEqualTo(id);
        List<FollowDetail> followDetailList = followDetailMapper.selectByExample(followDetailExample);
        return followDetailList;
    }

    @Override
    public PageInfo<SaleChance> findAllChance(Integer pageNo, Integer userId) {
        PageHelper.startPage(pageNo,10);

        List<SaleChance> list = saleChanceMapper.findAllMyChance(userId);
        return new PageInfo<>(list);
    }

    @Override
    public SaleChance findSaleChanceById(Integer id) {

        SaleChance saleChance = (SaleChance) saleChanceMapper.findSaleChanceById(id);
        return saleChance;
    }
}
