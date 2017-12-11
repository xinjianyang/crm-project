package com.kaishengit.controller.interceptor;

import com.kaishengit.auth.ShiroUtil;
import com.kaishengit.controller.exception.ForbiddenException;
import com.kaishengit.controller.exception.NotFoundException;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Customer;
import com.kaishengit.entity.Dept;
import com.kaishengit.service.CustomerService;

import com.kaishengit.service.DeptService;
import com.kaishengit.service.exception.ServiceException;
import com.kaishengit.web.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/11/9.
 */

@Controller
public class CustomerController {


    @Autowired
    private CustomerService customerService;





    @GetMapping("/mycustomer")
    public String customerList(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
            Model model){
        Admin admin = ShiroUtil.getCurrentAdmin();
        model.addAttribute("pageInfo",customerService.findMyCustomer(pageNo, admin.getId()));
        return "customer/list";
    }

    /**
     * 跳转到添加员工界面
     * @param model
     * @return
     */
    @GetMapping("/customer/new")
    public String customerNew(Model model){


        List<String> business = customerService.findAllBusiness();
        List<String> sources = customerService.findAllSources();


        model.addAttribute("sources",sources);
        model.addAttribute("business",business);
        return "customer/new";
    }


    @GetMapping("/customer/public/list")
    public String publicCustomer(Model model){
        List<Customer> customerList = customerService.findAllPublicCustomer();
        model.addAttribute("customerList",customerList);
        return "customer/public";
    }

    @PostMapping("/customer/new")
    public String customerNew(Customer customer, RedirectAttributes redirectAttributes, HttpSession session){
        Admin admin = ShiroUtil.getCurrentAdmin();
        customer.setUserId(admin.getId());
        customer.setUpdatetime(new Date());
        customerService.saveCustomer(customer);
        redirectAttributes.addFlashAttribute("message","添加成功");

        return "redirect:/mycustomer";
    }


    @GetMapping("/customer/detail/{id:\\d+}")
    public String customerDetail(@PathVariable Integer id, HttpSession session, Model model){

        Customer customer = checkCustomer(id,session);

        model.addAttribute("customer",customer);
        return "customer/detail";
    }

    private Customer checkCustomer(Integer id, HttpSession session){
        Customer customer = customerService.findById(id);
        Admin admin = ShiroUtil.getCurrentAdmin();
        if(customer == null){
            throw new NotFoundException("您请求的资源不存在");
        }
        if(!admin.getId().equals(customer.getUserId())){
            throw new ForbiddenException("没有访问权限");
        }
        return customer;
    }


    @GetMapping("/customer/edit/{id:\\d+}")
    public String editCustomer(@PathVariable Integer id, HttpSession session, Model model){

        Customer customer = checkCustomer(id,session);
        List<String> businesses = customerService.findAllBusiness();
        List<String> sourceses = customerService.findAllSources();
        model.addAttribute("businesses",businesses);
        model.addAttribute("sourceses",sourceses);
        model.addAttribute("customer",customer);
        return "customer/edit";
    }

    @PostMapping("/customer/edit")
    public String updateCustomer(Customer customer, RedirectAttributes redirectAttributes){

        customerService.updateCustomer(customer);
        redirectAttributes.addFlashAttribute("message","信息更新成功");
        return "redirect:/customer/detail/" + customer.getId();
    }

    @GetMapping("/customer/my/{id:\\d+}/public")
    public String intoPublic(@PathVariable Integer id, RedirectAttributes redirectAttributes,HttpSession session){
        Customer customer = checkCustomer(id, session);
        customerService.publicCustomer(customer);
        redirectAttributes.addFlashAttribute("message","放入公海成功");
        return "redirect:/mycustomer";
    }


    @GetMapping("/customer/export/csv")
    public void exportCsvFile(HttpServletResponse response) throws IOException {

        Admin admin = ShiroUtil.getCurrentAdmin();

        OutputStream outputStream = response.getOutputStream();
        response.setContentType("text/csv;charset=GBK");
        String fileName = new String("我的客户.csv".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
        customerService.exportCsvFileToOutputStream(outputStream, admin);
    }

    @GetMapping("/customer/export/xls")
    public void exportXlsFile(HttpServletResponse response) throws IOException {
        Admin admin = ShiroUtil.getCurrentAdmin();
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        String fileName = new String("我的客户.xls".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
        customerService.exportXlsFileToOutputStream(outputStream, admin);

    }

    @GetMapping("/customer/public/detail/{id:\\d+}")
    public String findPublicById(@PathVariable Integer id, Model model){
        Customer customer = customerService.findById(id);
        model.addAttribute("customer",customer);
        return "customer/publicDetail";
    }

    @GetMapping("/customer/changeTomine/{id:\\d+}")
    public String changeToMine(@PathVariable Integer id, HttpSession session){
        Admin admin = ShiroUtil.getCurrentAdmin();
        customerService.privateCustomer(id, admin);
        return "redirect:/mycustomer";
    }

    @GetMapping("/customer/delete/{id:\\d+}")
    @ResponseBody
    public ResultJson deleteById(@PathVariable Integer id, HttpSession session){
        Customer customer = checkCustomer(id,session);
        try {
            customerService.deleteCustomer(customer);
            return ResultJson.success();
        }catch (ServiceException ex){
            return ResultJson.error(ex.getMessage());
        }

    }

}
