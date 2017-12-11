package com.kaishengit.controller;

import com.kaishengit.auth.ShiroDbRealm;
import com.kaishengit.auth.ShiroUtil;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Task;
import com.kaishengit.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 代办事项控制器
 * Created by User on 2017/11/14.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 新增待办事项
     * @return
     */
    @GetMapping("/new")
    public String newTask(){
        return "task/new";
    }

    /**
     * 新增待办事项
     * @return
     */
    @PostMapping("/new")
    public String saveNewTask(String title, String finishTime, String remindTime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Task task = new Task();
        task.setTitle(title);
        task.setFinishTime(simpleDateFormat.parse(finishTime));
        task.setRemindTime(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(remindTime));
        Admin admin = ShiroUtil.getCurrentAdmin();
        taskService.saveNewTask(task,admin,remindTime);
        return "redirect:/task/list";
    }

    /**
     * 代办事项列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String findTaskList( Model model){

        Admin admin = ShiroUtil.getCurrentAdmin();

        List<Task> taskList = taskService.findAllMyTask(admin);
        model.addAttribute("taskList",taskList);
        return "task/list";
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}/del")
    public String delTaskById(@PathVariable Integer id){
        taskService.deleteTaskById(id);
        return "redirect:/task/list";
    }

    /**
     * 修改任务状态
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}/state/done")  ///task/"+id+"/state/done
    public String updateState(@PathVariable Integer id){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + id);
        taskService.updateState(id);

        return "redirect:/task/list";
    }

    /**
     * 修改任务状态
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}/state/undone")
    public String updateUndone(@PathVariable Integer id){
        taskService.updateStateToUndone(id);
        return "redirect:/task/list";
    }
}
