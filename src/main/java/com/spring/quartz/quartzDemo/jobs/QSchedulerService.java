package com.spring.quartz.quartzDemo.jobs;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.jdbcjobstore.MSSQLDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class QSchedulerService{

    @Autowired
    private Scheduler scheduler;

    MSSQLDelegate del;

    @PostMapping("/event")
    public String postEventSchedules(@RequestBody ScheduleRequest scheduleRequest){
        log.info("Inside Post Event Schedules");
        ZonedDateTime dateTime = ZonedDateTime.of(scheduleRequest.getDateTime(), scheduleRequest.getTimeZone());
        try{
            JobDetail jobDetail = buildStartJobDetail(scheduleRequest);  
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

        }catch(Exception e){
            e.printStackTrace();
            log.error("Error occurred while creating scheduler");
        }
        
        log.info("Existing Post Event Schedules");
        return "Scheduling Completed";
    }

    private JobDetail buildStartJobDetail(ScheduleRequest scheduleRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("MyData","ItsMyData" );
        
        return JobBuilder.newJob(StartJob.class)
                .withIdentity(UUID.randomUUID().toString(), "start-jobs")
                .withDescription("Start Event Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
                
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "start-triggers")
                .withDescription("Start Job trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
    
}
