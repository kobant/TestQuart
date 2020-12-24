package com.example.demo.service;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/22 15:53
 */

import org.quartz.SchedulerException;

/**
 * 初始化定时任务
 */

public interface ScheduleJobService {
	void initSchedule() throws SchedulerException;
}
