package com.kaishengit.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

import com.kaishengit.entity.Dept;
import com.kaishengit.entity.User;
import com.kaishengit.service.AdminService;
import com.kaishengit.service.DeptService;
import com.kaishengit.service.UserService;
import com.kaishengit.service.exception.ServiceException;

import com.kaishengit.web.DataTablesJson;
import com.kaishengit.web.ResultJson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/11/6.
 */

@Controller
public class UserController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;



    /**
     * 跳转新增用户界面
     * @return
     */
    @GetMapping("/user")
    public String userSave(Model model){
        List<Dept> deptList = deptService.findAllDept();
        model.addAttribute("deptList",deptList);
        return "user/new";
    }

    /**
     * 新增用户
     * @param user
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/user")
    public String userSave(User user,Integer deptId, RedirectAttributes redirectAttributes){
        userService.save(user);
        Integer uid = user.getId();
        userService.saveUserDept(uid, deptId);
        redirectAttributes.addFlashAttribute("message","保存成功");
        return "redirect:/user/list";
    }

    /**
     * 跳转用户列表界面
     * @param model
     * @param pageNo
     * @return
     */
    @GetMapping("/user/list")
    public String userList(Model model, @RequestParam(required = false, name = "p", defaultValue= "1") int pageNo){
        PageInfo<User> pageInfo = userService.findAllUserWithDept(pageNo);
        model.addAttribute("pageInfo",pageInfo);
        return "user/list";
    }

    /**
     * 创建新部门
     * @param deptName
     * @return
     */
    @PostMapping("/user/dept/new")
    @ResponseBody
    public ResultJson newDept(String deptName){
        try {
            userService.saveNewDept(deptName);
            return ResultJson.success();
        }catch (ServiceException ex){
            ex.printStackTrace();
            return ResultJson.error(ex.getMessage());
        }

    }

    /**
     * 获取用户列表,返回数据
     * @param draw
     * @param start
     * @param length
     * @param deptId
     * @param request
     * @return
     */
    @GetMapping("/user/load.json")
    @ResponseBody
    public DataTablesJson<User> loadUserList(Integer draw, Integer start, Integer length, Integer deptId, HttpServletRequest request){

        Map<String, Object> params = Maps.newHashMap();
        String keyWord = request.getParameter("search[value]");
        params.put("start",start);
        params.put("length",length);
        params.put("userName",keyWord);
        params.put("deptId",deptId);

        List<User> userList = userService.pageForUser(params);
        Long total = userService.userCountByDeptId(deptId);

        return new DataTablesJson<>(draw,total.intValue(),userList);

    }
    /*
    * 获取部门名称
    *
    * */
    @GetMapping("/user/dept.json")
    @ResponseBody
    public List<Dept> findAllDept(){
        return  userService.findAllDept();
    }


    /**
     * 去登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "admin/login";

    }

    /**
     * 获取登录验证
     * @param userName
     * @param password
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/login")
    public String login(String userName, String password, RedirectAttributes redirectAttributes,HttpServletRequest request){

        try{
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName,new Md5Hash(password).toString());

            subject.login(usernamePasswordToken);

           /* Session session = subject.getSession();
            Admin admin = (Admin) subject.getPrincipal();
            session.setAttribute("admin",admin);*/
            String url = "/admin/home";

            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if(savedRequest != null){
                url = savedRequest.getRequestUrl();
            }
            return "redirect:" + url;

        }catch (AuthenticationException ex){
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
            return "redirect:/login";
        }
    }

    /**
     * 登录成功之后,跳转到home.jsp
     * @return
     */
    @GetMapping("/admin/home")
    public String loginSuccess(){
        return "admin/home";
    }

    /**
     * 安全退出
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/loginout")
    public String loginOut( RedirectAttributes redirectAttributes){

        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message","您已安全退出");

        return "redirect:/login";
    }
}
