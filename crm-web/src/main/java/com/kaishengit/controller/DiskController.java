package com.kaishengit.controller;

import com.kaishengit.auth.ShiroUtil;
import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Disk;
import com.kaishengit.service.DiskService;
import com.kaishengit.web.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by User on 2017/11/16.
 */
@Controller
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private DiskService diskService;



    @GetMapping("/list")
    public String showDiskList(Model model, @RequestParam(defaultValue = "0",required = false, name = "pid") Integer pid){

        List<Disk> diskList = diskService.findAllDisk(pid);
        model.addAttribute("diskList",diskList);
        if(pid != 0){
            Disk disk = diskService.findById(pid);
            model.addAttribute("disk",disk);
        }
        return "disk/list";
    }

    /**
     * 创建文件夹
     * @param disk
     * @return
     */
    @PostMapping("/new/folder")
    @ResponseBody
    public ResultJson saveNewFolder(Disk disk){
        diskService.saveNewFolder(disk);
        List<Disk> diskList = diskService.findAllDisk(disk.getpId());
        return ResultJson.successWithData(diskList);
    }

    @GetMapping("/del/folder")
    @ResponseBody
    public ResultJson delFolder(Integer id){
        Admin admin = ShiroUtil.getCurrentAdmin();
        diskService.deleteFolderById(id,admin);
        return ResultJson.success();
    }



}
