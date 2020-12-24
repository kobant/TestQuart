package com.example.demo.config;

import com.example.demo.factory.JobFactory;
import org.quartz.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/22 15:16
 */
@Configuration
public class QuartzConfig {

	@Autowired
	private JobFactory jobFactory;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(){
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		try {
			//覆盖原来的任务
			schedulerFactoryBean.setOverwriteExistingJobs(true);

			//容器工厂
			schedulerFactoryBean.setJobFactory(jobFactory);
		}catch (Exception e){
			e.printStackTrace();
		}
		return schedulerFactoryBean;
	}

	//创建schedule
	@Bean(name = "scheduler")
	public Scheduler scheduler(){
		return schedulerFactoryBean().getScheduler();
	}
}
