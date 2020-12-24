package com.example.demo.controller;

import com.example.demo.enums.JobStatusEnum;
import com.example.demo.service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/23 14:00
 */
@RestController
@RequestMapping("/job")
public class JobsController {

	@Autowired
    private JobsService jobsService;

	@PostMapping("/slectAll")
	public List<Map> slectAll(){
		List<Map> list = jobsService.list();
		return list;
	}

	@PostMapping("/edit")
	public String edit(String name,
					   String content,
					   String cron_expression,
					   String class_method,
					   String job_group,
					   Long id){
		Map job= jobsService.get(id);
		if (JobStatusEnum.RUNNING.getCode().equals(job.get("state"))){
			return "修改之前请先停止任务!";
		}
		jobsService.updateJob(name, content, cron_expression, class_method, job_group, id, job.get("state").toString());

		return "success";
	}

	@PostMapping("/changeStatus/{id}")
	public String changeStatus(@PathVariable("id") Long id, Boolean jobStatus) {
		String status = jobStatus == true ? JobStatusEnum.RUNNING.getCode() : JobStatusEnum.STOP.getCode();
		try {
			jobsService.changeStatus(id, status);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "任务状态修改失败";
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove/{id}")
	public String remove(@PathVariable("id") Long id) {
		Map taskServer = jobsService.get(id);
		if (JobStatusEnum.RUNNING.getCode().equals(taskServer.get("state").toString())) {
			return "删除前请先停止任务！";

		}
		if (jobsService.remove(id) > 0) {
			return "success";
		}
		return "删除任务失败！ ";
	}

	/**
	 * 批量删除
	 */
	@PostMapping("/removeBatch")
	public String removeBatch(@RequestParam("ids[]") Long[] ids) {
		for (Long id : ids) {
			Map taskServer = jobsService.get(id);
			if (JobStatusEnum.RUNNING.getCode().equals(taskServer.get("state").toString())) {
				return "删除前请先停止任务！";
			}
		}
		jobsService.removeBatch(ids);
		return "success";
	}

	/**
	 * 新增保存
	 */
	@PostMapping("/save")
	public String save(
			@RequestParam(value = "name",required = false,defaultValue = "")String name,
			@RequestParam(value = "content",required = false,defaultValue = "")String content,
			@RequestParam(value = "cron_expression",required = false,defaultValue = "")String cron_expression,
			@RequestParam(value = "class_method",required = false,defaultValue = "")String class_method,
			@RequestParam(value = "job_group",required = false,defaultValue = "")String job_group
	) {
		if (jobsService.save(name, content, cron_expression, class_method, job_group) > 0) {
			return "success";
		}
		return "新增任务失败！";
	}
}
