package com.example.demo.listenner;


import com.example.demo.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

	@Autowired
	private ScheduleJobService scheduleJobService;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			//初始化
			scheduleJobService.initSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
