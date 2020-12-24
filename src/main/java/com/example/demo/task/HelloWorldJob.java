package com.example.demo.task;

import com.example.demo.service.JobsService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/23 14:53
 */
@DisallowConcurrentExecution
@Component
public class HelloWorldJob implements Job {

	@Autowired
	private JobsService jobsService;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("欢迎使用定时任务 "+ new Date());
		List<Map> list = jobsService.list();
		System.out.println("定时查询："+list);

		System.out.println("定时清除：");
		jobsService.remove(Long.valueOf(36));

	}


}
