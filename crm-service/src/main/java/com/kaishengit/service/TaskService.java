package com.kaishengit.service;

import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Task;

import java.util.List;

/**
 * Created by User on 2017/11/14.
 */
public interface TaskService {

    void saveNewTask(Task task, Admin admin, String remindTime);

    List<Task> findAllMyTask(Admin admin);

    void deleteTaskById(Integer id);

    void updateState(Integer id);

    void updateStateToUndone(Integer id);

}
