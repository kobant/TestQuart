package com.example.demo.service.impl;

import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.JobsMapper;
import com.example.demo.service.JobsService;
import com.example.demo.utils.QuartzManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/23 14:04
 */
@Service
public class JobsServiceImpl implements JobsService {

	@Autowired
	private JobsMapper jobsMapper;

	@Autowired
	QuartzManager quartzManager;

	@Override
	public Map get(Long id) {
		return jobsMapper.get(id);
	}

	@Override
	public List<Map> list() {
		List<Map> list = jobsMapper.list();
		return list;
	}

	@Override
	public int save(String name, String content, String cron_expression, String class_method, String group) {
		//进行封装包装

		HashMap<String, Object> job = new HashMap<>();
		job.put("name", name);
		job.put("content", content);
		job.put("cron_expression", cron_expression);
		job.put("class_method", class_method);
		job.put("job_group", group);
		job.put("create_time", new Date());
		job.put("update_time", new Date());
		//默认关闭定时任务
		job.put("state", 0);
		return jobsMapper.save(job);
	}

	@Override
	public int updateJob(String name, String content, String cron_expression, String class_method, String job_group, Long id, String state) {
		HashMap<String, Object> job = new HashMap<>();
		job.put("name", name);
		job.put("content", content);
		job.put("cron_expression", cron_expression);
		job.put("class_method", class_method);
		job.put("job_group", job_group);
		job.put("update_time", new Date());
		job.put("id", id);
		job.put("state", state);
		return jobsMapper.update(job);
	}

	@Override
	public int remove(Long id) {
		try {
			Map job = get(id);
			quartzManager.deleteJob(job);
			return jobsMapper.remove(id);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return 0;
	}



	@Override
	public int removeBatch(Long[] ids) {
		for (Long id:ids){
			try {
				Map task = get(id);
				quartzManager.deleteJob(task);
			}catch (SchedulerException  e){
				e.printStackTrace();
				return 0;
			}
		}
		return jobsMapper.removeBatch(ids);
	}

	@Override
	public void changeStatus(Long jobId, String jobStatus) throws SchedulerException {

		Map job = get(jobId);
		if (job == null) {
			return;
		}
		if (JobStatusEnum.STOP.getCode().equals(jobStatus)) {
			//暂停job
			quartzManager.deleteJob(job);
			job.put("state", 0);
		} else {
			job.put("state", 1);
			//直接启动
			quartzManager.addJob(job);
		}
		updateJob(job.get("name").toString(), job.get("content").toString(), job.get("cron_expression").toString(), job.get("class_method").toString(), job.get("job_group").toString(), jobId, job.get("state").toString());

	}

	@Override
	public void updateCron(Long jobId) throws SchedulerException {
		Map job = get(jobId);
		if (job == null) {
			return;
		}
		if (JobStatusEnum.RUNNING.getCode().equals(job.get("state").toString())) {
			quartzManager.updateJobCron(job);
		}
		updateJob(job.get("name").toString(), job.get("content").toString(), job.get("cron_expression").toString(), job.get("class_method").toString(), job.get("job_group").toString(), jobId, job.get("state").toString());
	}
	}



