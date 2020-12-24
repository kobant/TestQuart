package com.example.demo.utils;


import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 计划任务管理
 */
@Service
public class QuartzManager {

    @Autowired
    private Scheduler scheduler;

    /**
     * 添加任务到定时任务中
     *
     * @param job
     */
    public void addJob(Map job) {
        try {

            // 启动调度器
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(job.get("class_method").toString()).newInstance()
                    .getClass());
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.get("name").toString(), job.get("job_group").toString())// 任务名称和组构成任务key
                    .build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.get("name").toString(), job.get("job_group").toString())// 触发器key
                    .startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.get("cron_expression").toString())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 暂停任务
     *
     * @param job
     * @throws SchedulerException
     */
    public void pauseJob(Map job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.get("name").toString(), job.get("group").toString());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param job
     * @throws SchedulerException
     */
    public void resumeJob(Map job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.get("name").toString(), job.get("group").toString());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param job
     * @throws SchedulerException
     */
    public void deleteJob(Map job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.get("name").toString(), job.get("job_group").toString());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param job
     * @throws SchedulerException
     */
    public void runJobNow(Map job) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(job.get("name").toString(), job.get("group").toString());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param job
     * @throws SchedulerException
     */
    public void updateJobCron(Map job) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(job.get("name").toString(), job.get("job_group").toString());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.get("cron_expression").toString());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}