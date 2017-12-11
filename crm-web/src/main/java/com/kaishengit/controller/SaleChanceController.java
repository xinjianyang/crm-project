package com.kaishengit.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.auth.ShiroUtil;
import com.kaishengit.controller.exception.ForbiddenException;
import com.kaishengit.controller.exception.NotFoundException;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Customer;
import com.kaishengit.entity.FollowDetail;
import com.kaishengit.entity.SaleChance;
import com.kaishengit.service.CustomerService;
import com.kaishengit.service.SaleChanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by User on 2017/11/13.
 */
@Controller
public class SaleChanceController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/saleChance/my")
    public String saleChanceMy(@RequestParam(defaultValue = "1", required = false, name = "pageNo") Integer pageNo, Model model){
        Admin admin = ShiroUtil.getCurrentAdmin();

        PageInfo<SaleChance> pageInfo = saleChanceService.findAllChance(pageNo, admin.getId());
        model.addAttribute("pageInfo",pageInfo);
        return "saleChance/mychanceList";
    }

    @GetMapping("/saleChance/MyDetail/{id:\\d+}")
    public String findSaleChanceById(@PathVariable Integer id, Model model){

        SaleChance saleChance = saleChanceService.findSaleChanceById(id);
        if(saleChance == null){
            throw new NotFoundException("该客户不存在");
        }
        Admin admin = ShiroUtil.getCurrentAdmin();
        if(!saleChance.getUserId().equals(admin.getId())){
            throw new ForbiddenException("没有权限访问");
        }

        List<FollowDetail> followDetailList = saleChanceService.findAllFollowBySaleId(id);

        model.addAttribute("followList",followDetailList);
        model.addAttribute("saleChance",saleChance);
        return "saleChance/mySaleChance";
    }

    @GetMapping("/saleChance/new")
    public String newSaleChance(Model model){
        Admin admin = ShiroUtil.getCurrentAdmin();
        List<Customer> customerList = customerService.findAllCustomerMy(admin.getId());
        model.addAttribute("customerList",customerList);
        model.addAttribute("progressList",saleChanceService.findAllProgress());
        return "saleChance/new";
    }

    @PostMapping("/saleChance/new")
    public String saveSaleChance(SaleChance saleChance){
        Admin admin = ShiroUtil.getCurrentAdmin();


        saleChanceService.saveNewSaleChance(saleChance,admin);

        return "redirect:/saleChance/my";
    }

    @PostMapping("/follow/add")
    public String saveFollow(FollowDetail followDetail, Integer customerId){

        saleChanceService.saveNewFollowDetail(followDetail, customerId);
        return "redirect:/saleChance/MyDetail/" + followDetail.getSaleId();
    }

    @GetMapping("/saleChance/public")
    public String saleChancePublic(){
        return "saleChance/pubilc";
    }
}
