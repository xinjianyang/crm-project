package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.FollowDetail;
import com.kaishengit.entity.SaleChance;

import java.util.List;

/**
 * Created by User on 2017/11/13.
 */
public interface SaleChanceService {

    PageInfo<SaleChance> findAllChance(Integer pageNo, Integer userId);

    SaleChance findSaleChanceById(Integer id);

    List<String> findAllProgress();

    void saveNewSaleChance(SaleChance saleChance, Admin admin);

    void saveNewFollowDetail(FollowDetail followDetail, Integer customerId);

    List<FollowDetail> findAllFollowBySaleId(Integer id);

}
