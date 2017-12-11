package com.kaishengit.test;


import com.kaishengit.service.impl.jobDetail.MyJob;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by User on 2017/11/15.
 */

public class TimerTestCase {

/*
    @Test
    public void timerTest(){

        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(),0, 2000);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @Test
    public void quartzTest() throws SchedulerException, IOException {

        JobDetail detail = JobBuilder.newJob(MyJob.class).build();

        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.scheduleJob(detail, trigger);
        scheduler.start();

        System.in.read();
    }

}
