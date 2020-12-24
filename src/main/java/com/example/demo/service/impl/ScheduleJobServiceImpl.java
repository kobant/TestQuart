package com.example.demo.service.impl;

import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.JobsMapper;
import com.example.demo.service.ScheduleJobService;
import com.example.demo.utils.QuartzManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/22 15:54
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

	@Autowired
	private JobsMapper jobsMapper;

	@Autowired
    private QuartzManager quartzManager;

	@Override
	public void initSchedule() throws SchedulerException {
       //这里获取任务信息数据
		List<Map> jobList = jobsMapper.list();
		for (Map map :jobList){
			if (JobStatusEnum.RUNNING.getCode().equals(map.get("state"))){
				quartzManager.addJob(map);
			}
		}
	}
}
