package com.example.demo.service;

import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/23 14:02
 */

public interface JobsService {

	Map get(Long id);

	List<Map> list();

	int save(String name, String content, String cron_expression, String class_method, String group);

	int updateJob(String name, String content, String cron_expression, String class_method, String job_group, Long id, String state);

	int remove(Long id);

	int removeBatch(Long[] ids);


	void changeStatus(Long jobId, String jobStatus) throws SchedulerException;

	void updateCron(Long jobId) throws SchedulerException;
}
