package com.kaishengit.service.impl;

import com.kaishengit.entity.Admin;
import com.kaishengit.entity.Task;
import com.kaishengit.entity.TaskExample;

import com.kaishengit.mapper.TaskMapper;
import com.kaishengit.service.TaskService;

import com.kaishengit.service.exception.ServiceException;
import com.kaishengit.service.impl.jobDetail.MyJob;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/11/14.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * 创建新的任务提醒
     * @param task
     * @param admin
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveNewTask(Task task, Admin admin, String remindTime) {

        task.setStatus(0);
        task.setCreateTime(new Date());
        task.setUserId(admin.getId());

        taskMapper.insertSelective(task);
        logger.info("创建新的待办事项{}" , task.getTitle());

        //添加新的调度任务
        if(StringUtils.isNotEmpty(task.getRemindTime().toString())){

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("userId",task.getUserId());
            jobDataMap.put("message",task.getTitle());

            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    .setJobData(jobDataMap)
                    .withIdentity(new JobKey("taskId:" + task.getId(),"sendMessageGroup"))
                    .build();
/*            Date date = task.getRemindTime();
            StringBuilder cron = new StringBuilder("0")
                    .append(" ")
                    .append(date.getMinutes())
                    .append(" ")
                    .append(date.getHours())
                    .append(" ")
                    .append(date.getDay())
                    .append(" ")
                    .append(date.getMonth())
                    .append(" ? ")
                    .append(date.getYear());
            logger.info("CRON {}>>>>>>", cron.toString());
            System.out.println(date);*/


            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

            DateTime dateTime = formatter.parseDateTime(remindTime);

            StringBuilder cron = new StringBuilder("0")
                    .append(" ")
                    .append(dateTime.getMinuteOfHour())
                    .append(" ")
                    .append(dateTime.getHourOfDay())
                    .append(" ")
                    .append(dateTime.getDayOfMonth())
                    .append(" ")
                    .append(dateTime.getMonthOfYear())
                    .append(" ? ")
                    .append(dateTime.getYear());

            logger.info("CRON : {}" ,cron.toString());

            ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron.toString());

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(scheduleBuilder)
                    .build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            try {
                scheduler.scheduleJob(jobDetail,trigger);
                scheduler.start();
            } catch (SchedulerException e) {
                throw new ServiceException("添加定时任务异常",e);
            }



        }
    }

    /**
     * 获得当前用户的所有计划任务
     * @param admin
     * @return
     */
    @Override
    public List<Task> findAllMyTask(Admin admin) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andUserIdEqualTo(admin.getId());
        List<Task> taskList = taskMapper.selectByExample(taskExample);
        return taskList;
    }

    /**
     * 根据id删除任务
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteTaskById(Integer id) {

        Task task = taskMapper.selectByPrimaryKey(id);
        taskMapper.deleteByPrimaryKey(id);

        if(StringUtils.isNotEmpty(task.getRemindTime().toString())){
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            try {
                scheduler.deleteJob(new JobKey("taskId:" + id,"sendMessageGroup"));
                logger.info("成功删除定时任务{}" + id);
            } catch (SchedulerException e) {
                throw new ServiceException("删除定时任务异常",e);
            }
        }

    }

    /**
     * 更新任务状态 已完成
     * @param id
     */
    @Override
    public void updateState(Integer id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        task.setStatus(1);
        taskMapper.updateByPrimaryKeySelective(task);

    }

    /**
     * 更新任务状态 改为未完成
     * @param id
     */
    @Override
    public void updateStateToUndone(Integer id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        task.setStatus(0);
        taskMapper.updateByPrimaryKeySelective(task);
    }
}
